/*
 * This file is generated by jOOQ.
 */
package pay.token.jooq.schema.public_;


import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;

import pay.token.jooq.schema.public_.tables.Author;
import pay.token.jooq.schema.public_.tables.records.AuthorRecord;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * PUBLIC.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<AuthorRecord> CONSTRAINT_7 = Internal.createUniqueKey(Author.AUTHOR, DSL.name("CONSTRAINT_7"), new TableField[] { Author.AUTHOR.ID }, true);
}
