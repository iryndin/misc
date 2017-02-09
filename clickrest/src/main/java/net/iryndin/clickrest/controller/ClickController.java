package net.iryndin.clickrest.controller;

import net.iryndin.clickrest.service.IClickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.String.format;

/**
 * @author iryndin
 * @since 09/02/17
 */
@RestController
public class ClickController {

    IClickService clickService;

    @Autowired
    public ClickController(IClickService clickService) {
        this.clickService = clickService;
    }

    @RequestMapping(path = "/banner/{id}/click", method = RequestMethod.POST)
    public void register(@PathVariable("id") String bannerId, @RequestParam("cost") Integer cost) {
        clickService.registerClick(bannerId, cost);
    }

    @RequestMapping(path = "/banner/{id}/click", method = RequestMethod.GET)
    public long stats(@PathVariable("id") String bannerId) {
        return clickService.getStats(bannerId).orElseThrow(() ->
                new ResourceNotFoundException(format("Banner '%s' cannot be found", bannerId)));
    }
}
