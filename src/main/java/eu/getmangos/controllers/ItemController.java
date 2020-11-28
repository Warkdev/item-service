package eu.getmangos.controllers;

import java.util.Date;
import java.util.List;

import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.reactivestreams.Publisher;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;

import eu.getmangos.entities.Item;
import eu.getmangos.mapper.ItemMapper;

@ApplicationScoped
public class ItemController {
    @Inject private Logger logger;

    @PersistenceContext(unitName = "WORLD_PU")
    private EntityManager emWorld;

    @PersistenceContext(unitName = "DBC_PU")
    private EntityManager emDbc;

    @Inject private ItemMapper mapper;

    @Transactional
    /**
     * Creates an item in the dabatase.
     * @param item The item to create.
     * @throws DAOException Send a DAOException if something happened during the data validation.
     */
    public void create(Item item) throws DAOException {
        logger.debug("create() entry.");
        // Add validations here

        emWorld.persist(item);
        logger.debug("create() exit.");
    }

    @Transactional
    /**
     * Updates an Item in the dabatase.
     * @param Item The Item to edit.
     * @throws DAOException Send a DAOException if something happened during the data validation.
     */
    public void update(Item item) throws DAOException {
        logger.debug("update() entry.");
        // Add validations here

        emWorld.merge(item);
        logger.debug("update() exit.");
    }

    /**
     * Delete an Item in the database. This method will also delete all links with the bans for this Item.
     * @param id The ID of the Item to be deleted.
     * @throws DAOException Send a DAOException if something happened during the data validation.
     */
    @Transactional
    public void delete(Integer id) throws DAOException {
        logger.debug("delete() entry.");

        Item item = find(id);
        if(item == null) {
            logger.debug("delete() exit.");
            throw new DAOException("The item doesn't exist");
        }

        emWorld.remove(item);

        logger.debug("delete() exit.");
    }

    /**
     * Retrieves an item by its ID.
     * @param id The ID of the item
     * @return The item if found, null otherwise.
     */
    public Item find(Integer id) {
        logger.debug("find() entry.");
        try {
            Item item = (Item) emWorld.createNamedQuery("Item.findById").setParameter("id", id).getSingleResult();
            logger.debug("find() exit.");
            return item;
        } catch (NoResultException nre) {
            logger.debug("No item found with this id.");
            logger.debug("find() exit.");
            return null;
        }
    }
    /**
     * Retrieves all items from the database.
     * @return A list of Item.
     */
    @SuppressWarnings("unchecked")
    public List<Item> findAll() {
        return (List<Item>) emWorld.createNamedQuery("Item.findAll").getResultList();
    }

    }
