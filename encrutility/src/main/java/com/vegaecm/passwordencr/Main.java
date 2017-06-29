package com.vegaecm.passwordencr;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * @author iryndin
 * @since 15/06/17
 */
public class Main {

  private Settings settings;
  private List<File> files;

  static Logger log = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      log.error("Please provide missing settings file");
      System.exit(1);
    }
    String settingsPath = args[0];
    Main main = new Main();
    log.debug("Read settings");
    main.settings = readSettings(settingsPath);
    main.files = main.readListOfFiles();
    main.encryptPasswords();
  }

  private void encryptPasswords() {
    for (File f : files) {
      encryptPasswords(f);
    }
  }

  private boolean encryptPasswords(File f) {
    log.info("Start encrypting passwords for file: " + f);
    String fileContent;
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(f);
      fileContent = IOUtils.toString(fis, "UTF-8");
    } catch (IOException ioe) {
      log.error("ERROR: Cannot read file: " + f, ioe);
      return false;
    } finally {
      if (fis != null) {
        try {
          fis.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
        fis = null;
      }
    }
    Optional<String> encryptedContent = encryptPasswords(fileContent);
    if (encryptedContent.isPresent()) {
      String newFilename = f.getAbsolutePath() + ".encrypted";
      FileWriter fw = null;
      try {
        fw = new FileWriter(newFilename);
        fw.write(encryptedContent.get());
        log.info("Write file: " + newFilename);
        return true;
      } catch (IOException ioe) {
        log.error("ERROR: Cannot write file: " + f, ioe);
      } finally {
        if (fw != null) {
          try {
            fw.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
          fw = null;
        }
      }
    } else {
      log.info("Nothing found in file: " + f);
    }
    return false;
  }

  private Optional<String> encryptPasswords(String content) {
    String strToProcess = content;
    boolean changed = false;
    for (Settings.StartEndStr ses : settings.getSeList()) {
      Optional<String> wasProcessed = processString(strToProcess, ses);
      if (wasProcessed.isPresent()) {
        strToProcess = wasProcessed.get();
        changed = true;
      }
    }
    if (changed) {
      return Optional.of(strToProcess);
    } else {
      return Optional.absent();
    }
  }

  private Optional<String> processString(String content, Settings.StartEndStr ses) {
    int startIdx = 0;
    boolean changed = false;
    String strToProcess = content;
    int foundIdx;
    do {
      foundIdx = strToProcess.indexOf(ses.getStartStr(), startIdx);
      if (foundIdx >= 0) {
        int foundEndIdx = strToProcess.indexOf(ses.getEndStr(), foundIdx + ses.getStartStr().length());
        if (foundEndIdx >= foundIdx) {
          int leftIdx = foundIdx + ses.getStartStr().length();
          int rightIdx = foundEndIdx;
          String strToEncrypt = strToProcess.substring(leftIdx, rightIdx);
          String encryptedString = encrypt(strToEncrypt);
          log.debug("Plain string: " + strToEncrypt);
          log.debug("Encrypted string: " + encryptedString);
          strToProcess = strToProcess.substring(0, leftIdx) + encryptedString + strToProcess.substring(rightIdx);
          startIdx = foundEndIdx + ses.getEndStr().length();
          changed = true;
        } else {
          startIdx = foundIdx + ses.getStartStr().length();
        }
      }
    } while (foundIdx >=0);


    if (changed) {
      return Optional.of(strToProcess);
    } else {
      return Optional.absent();
    }
  }

  private String encrypt(String strToEncrypt) {
    //return "-- ENCRYPTED STRING --";
    try {
      return runUtility(strToEncrypt, settings.getEncrPath());
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  private String runUtility(String strToEncrypt, String commandline) throws Exception {
    final String SUFFIX = "Encrypted password: ";
    String path = commandline.replace("${str}", strToEncrypt);
    Process p = Runtime.getRuntime().exec(path);
    p.waitFor();
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.startsWith(SUFFIX)) {
          String result = line.substring(SUFFIX.length());
          return result.trim();
        }
      }
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
    String msg = "Could not encrypt this: " + strToEncrypt;
    log.error(msg);
    throw new IllegalStateException(msg);
  }

  private List<File> readListOfFiles() throws IOException {
    InputStreamReader isr = null;
    try {
      isr = new InputStreamReader(new FileInputStream(settings.getFilesPath()));
      List<String> filePaths = CharStreams.readLines(isr);
      List<File> result = Lists.newArrayList();
      for (String fp : filePaths) {
        result.add(new File(fp));
      }
      return result;
    } finally {
      if (isr != null) {
        isr.close();
        isr = null;
      }
    }
  }

  private static Settings readSettings(String settingsFilePath) throws IOException {
    InputStreamReader isr = null;
    try {
      isr = new InputStreamReader(new FileInputStream(settingsFilePath));
      Settings settings = new Settings();
      String yamlStr = CharStreams.toString(isr);
      Map<String, Object> rootYaml = (Map<String, Object>)new Yaml().load(yamlStr);
      Map<String, Object> settingsYaml = (Map<String, Object>)rootYaml.get("settings");
      String encrPath = (String)settingsYaml.get("encrcmdline");
      settings.setEncrPath(encrPath);
      settings.setFilesPath((String)settingsYaml.get("files"));
      List<Map<String, String>> boundaries = (List<Map<String, String>>)settingsYaml.get("boundaries");
      List<Settings.StartEndStr> seList = Lists.newArrayList();
      for (Map<String, String> m : boundaries) {
        Settings.StartEndStr a = new Settings.StartEndStr();
        a.setStartStr(m.get("start"));
        a.setEndStr(m.get("end"));
        seList.add(a);
      }
      settings.setSeList(seList);
      log.debug(settings.toString());
      return settings;
    } finally {
      if (isr != null) {
        isr.close();
      }
    }
  }
}
