/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package leap.orm.event;

import leap.orm.OrmContext;
import leap.orm.mapping.EntityMapping;

/**
 * The handler interface for handling events of entity (create, update, delete and load).
 */
public interface EntityEventHandler {

    /**
     * Returns true if handles the 'Create' event of the given entity.
     */
    boolean isHandleCreateEvent(OrmContext context, EntityMapping em);

    /**
     * Returns true if the 'Create' event should be handled in a transaction.
     */
    boolean isCreateEventTransactional(OrmContext context, EntityMapping em);

    /**
     * Executed before saving the entity record to db outside transaction.
     *
     * <p/>
     * The entity fields can be changed and all the changes will be saved to db.
     */
    void preCreateEntityNoTrans(OrmContext context, EntityMapping em, CreateEntityEvent e);

    /**
     * Executed before saving the entity record to db inside transaction.
     *
     * <p/>
     * The entity fields can be changed and all the changes will be saved to db.
     */
    void preCreateEntityInTrans(OrmContext context, EntityMapping em, CreateEntityEvent e);

    /**
     * Executed after saving the the entity record to db inside transaction.
     */
    void postCreateEntityInTrans(OrmContext context, EntityMapping em, CreateEntityEvent e);

    /**
     * Executed after saving the the entity record to db outside transaction.
     */
    void postCreateEntityNoTrans(OrmContext context, EntityMapping em, CreateEntityEvent e);

    /**
     * Returns true if handles the 'Update' event of the given entity.
     */
    boolean isHandleUpdateEvent(OrmContext context, EntityMapping em);

    /**
     * Returns true if the 'Update' event should be handled in a transaction.
     */
    boolean isUpdateEventTransactional(OrmContext context, EntityMapping em);

    /**
     * Executed before saving the entity record to db outside transaction.
     *
     * <p/>
     * The entity fields can be changed and all the changes will be saved to db.
     */
    void preUpdateEntityNoTrans(OrmContext context, EntityMapping em, UpdateEntityEvent e);

    /**
     * Executed before saving the entity record to db inside transaction.
     *
     * <p/>
     * The entity fields can be changed and all the changes will be saved to db.
     */
    void preUpdateEntityInTrans(OrmContext context, EntityMapping em, UpdateEntityEvent e);

    /**
     * Executed after saving the the entity record to db inside transaction.
     */
    void postUpdateEntityInTrans(OrmContext context, EntityMapping em, UpdateEntityEvent e);

    /**
     * Executed after saving the the entity record to db outside transaction.
     */
    void postUpdateEntityNoTrans(OrmContext context, EntityMapping em, UpdateEntityEvent e);

    /**
     * Returns true if handles the 'Delete' event of the given entity.
     */
    boolean isHandleDeleteEvent(OrmContext context, EntityMapping em);

    /**
     * Returns true if the 'Delete' event should be handled in a transaction.
     */
    boolean isDeleteEventTransactional(OrmContext context, EntityMapping em);

    /**
     * Executed before saving the entity record to db outside transaction.
     *
     * <p/>
     * The entity fields can be changed and all the changes will be saved to db.
     */
    void preDeleteEntityNoTrans(OrmContext context, EntityMapping em, DeleteEntityEvent e);

    /**
     * Executed before saving the entity record to db inside transaction.
     *
     * <p/>
     * The entity fields can be changed and all the changes will be saved to db.
     */
    void preDeleteEntityInTrans(OrmContext context, EntityMapping em, DeleteEntityEvent e);

    /**
     * Executed after saving the the entity record to db inside transaction.
     */
    void postDeleteEntityInTrans(OrmContext context, EntityMapping em, DeleteEntityEvent e);

    /**
     * Executed after saving the the entity record to db outside transaction.
     */
    void postDeleteEntityNoTrans(OrmContext context, EntityMapping em, DeleteEntityEvent e);

    /**
     * Returns true if handles 'PostLoad' event of the given entity.
     */
    boolean isHandleLoadEvent(OrmContext context, EntityMapping em);

    /**
     * Executed after load entity record.
     */
    void postLoadEntityNoTrans(OrmContext context, EntityMapping em, LoadEntityEvent e);
}