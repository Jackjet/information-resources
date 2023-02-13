package d1.project.container.api.base.factory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class ScriptEngineFactory {
    private final static ScriptEngineFactory SCRIPT_ENGINE_FACTORY = new ScriptEngineFactory();
    private ScriptEngine engine;

    private ScriptEngineFactory() {
        if (engine == null) {
            ScriptEngineManager manager = new ScriptEngineManager();
            engine = manager.getEngineByName("nashorn");
        }
    }

    public static ScriptEngineFactory getInstance() {
        return SCRIPT_ENGINE_FACTORY;
    }


    public ScriptEngine getScriptEngine() {
        return engine;
    }

}
