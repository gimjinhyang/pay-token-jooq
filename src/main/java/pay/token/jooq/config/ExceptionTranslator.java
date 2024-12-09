package pay.token.jooq.config;

import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultExecuteListener;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import java.io.Serial;

/**
 * ExecuteListener를 상속 받은 Exception Handler
 *
 * @author Jinhyang
 */
public class ExceptionTranslator extends DefaultExecuteListener implements ExecuteListener {

    @Serial
    private static final long serialVersionUID = -284807483120012088L;

    /*
     * (non-Javadoc)
     *
     * @see org.jooq.impl.DefaultExecuteListener#exception(org.jooq.ExecuteContext)
     */
    @Override
    public void exception(ExecuteContext context) {
        SQLDialect dialect = context.configuration().dialect();
        SQLExceptionTranslator translator = new SQLErrorCodeSQLExceptionTranslator(dialect.name());

        context.exception(
                translator.translate("Access database using jOOQ", context.sql(), context.sqlException()));
    }

}
