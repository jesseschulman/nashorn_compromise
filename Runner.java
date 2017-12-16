import jdk.nashorn.api.scripting.AbstractJSObject;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Runner {

    public Runner() {

    }

    // this throws ClassCastException from line 5377 of Compromise.js, which is:
    // ts.match('#Hyphenated #Hyphenated').match('#NumericValue #NumericValue').tag('NumberRange');
    // in the JSObject "Function" we call those same methods successfully when you uncomment line 5376 of Compromise.js
    // uncommenting this line leads to other errors but prevents the ClassCastException exception on line 5377
    public void run() throws FileNotFoundException, ScriptException {
        NashornScriptEngine engine = (NashornScriptEngine) new NashornScriptEngineFactory().getScriptEngine();
        engine.put("JSObject", new AbstractJSObject() {
            @Override
            public Object call(Object thiz, Object... args) {
                ((ScriptObjectMirror) ((ScriptObjectMirror) ((ScriptObjectMirror) args[0]).callMember("match", "#Hyphenated #Hyphenated")).callMember("match", "#NumericValue #NumericValue")).callMember("tag", "NumberRange");
                return null;
            }

            @Override
            public boolean isFunction() {
                return true;
            }
        });
        engine.eval(new FileReader(new File("./Compromise.js")));
        engine.eval("var t = nlp('dinosaur').nouns().toPlural();");
        engine.eval("print(t.out('text');");
    }

    public static void main(String[] args) throws FileNotFoundException, ScriptException {
        new Runner().run();
    }

}
