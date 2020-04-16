package wantsome.project.web;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.Map;

public class SparkUtil {

    /**
     * Utility method, 'combines' a model with a Velocity template file,
     * to render (build) a response page.
     * Recommended at: http://sparkjava.com/documentation#views-and-templates
     */
    public static String render(Map<String, Object> model, String templatePath) {
        return new VelocityTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
