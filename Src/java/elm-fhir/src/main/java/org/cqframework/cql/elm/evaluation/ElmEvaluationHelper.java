package org.cqframework.cql.elm.evaluation;

import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.elm.execution.IncludeDef;
import org.hl7.elm.r1.Library;
import org.hl7.elm.r1.Expression;
import org.hl7.elm.r1.VersionedIdentifier;
import org.opencds.cqf.cql.engine.execution.*;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ElmEvaluationHelper {

    // TODO: Improved library loader support...
    private static LibraryLoader libraryLoader = new DefaultLibraryLoader();

    public static Object evaluate(Library library, Expression value, Map<String, Object> parameters, ZonedDateTime evaluationDateTime) {
        // TODO: Cache for libraries?

        Map<VersionedIdentifier, Library> map = new HashMap<>();
        map.put(library.getIdentifier(), library);
        CqlEngine engine = getEngine(library, parameters, evaluationDateTime);
        engine.init(library.getIdentifier(), map,null, null, null, evaluationDateTime);
        return engine.visitExpression(value, engine.getState());
    }

    private static CqlEngine getEngine(Library library, Map<String, Object> parameters, ZonedDateTime evaluationDateTime) {
        Environment environment = new Environment(getLibraryManager());
        CqlEngine engine = new CqlEngine(environment);
        if (evaluationDateTime != null) {
            engine.getState().setEvaluationDateTime(evaluationDateTime);
        }
        engine.getState().setParameters(library, parameters);
        return engine;
    }

    protected static LibraryManager getLibraryManager() {
        LibraryManager libraryManager = new LibraryManager(getModelManager());
        libraryManager.getLibrarySourceLoader().registerProvider(new TestLibrarySourceProvider());
        return libraryManager;
    }

    protected static ModelManager getModelManager() {
        return new ModelManager();
    }


}