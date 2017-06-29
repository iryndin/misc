package com.vegaecm.passwordencr;

import java.util.List;

/**
 * @author iryndin
 * @since 23/06/17
 */
public class Settings {

  public static class StartEndStr {
    private String startStr;
    private String endStr;

    public String getStartStr() {
      return startStr;
    }

    public void setStartStr(String startStr) {
      this.startStr = startStr;
    }

    public String getEndStr() {
      return endStr;
    }

    public void setEndStr(String endStr) {
      this.endStr = endStr;
    }

    @Override
    public String toString() {
      return "StartEndStr{" +
              "startStr='" + startStr + '\'' +
              ", endStr='" + endStr + '\'' +
              '}';
    }
  }

  private List<StartEndStr> seList;
  private String encrPath;
  private String filesPath;

  public List<StartEndStr> getSeList() {
    return seList;
  }

  public void setSeList(List<StartEndStr> seList) {
    this.seList = seList;
  }

  public String getEncrPath() {
    return encrPath;
  }

  public void setEncrPath(String encrPath) {
    this.encrPath = encrPath;
  }

  public String getFilesPath() {
    return filesPath;
  }

  public void setFilesPath(String filesPath) {
    this.filesPath = filesPath;
  }

  @Override
  public String toString() {
    return "Settings{" +
            "seList=" + seList +
            ", encrPath='" + encrPath + '\'' +
            ", filesPath='" + filesPath + '\'' +
            '}';
  }
}
