package eu.getmangos.rest.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;

import eu.getmangos.controllers.ItemController;
import eu.getmangos.controllers.DAOException;
import eu.getmangos.dto.ItemDTO;
import eu.getmangos.dto.MessageDTO;
import eu.getmangos.entities.Item;
import eu.getmangos.mapper.ItemMapper;
import eu.getmangos.rest.ItemResource;

@ApplicationScoped
@Path("Item/v1")
public class ItemResourceService implements ItemResource {

    @Inject private Logger logger;

    @Inject private ItemController ItemController;

    @Inject private ItemMapper ItemMapper;

    public Response findItem(Integer id) {
        logger.debug("find() entry.");

        if (id == null) {
                return Response.status(500).entity(new MessageDTO("The provided ID is null.")).build();
        }

        ItemDTO Item = ItemMapper.map(ItemController.find(id));

        if(Item == null) {
                return Response.status(404).entity(new MessageDTO("The provided ID has no match in the database.")).build();
        }

        logger.debug("find() exit.");
        return Response.status(200).entity(Item).build();
    }

    public Response findAllItems() {
        logger.debug("findAll() entry.");

        List<ItemDTO> listItems = new ArrayList<>();
        for(Item a : this.ItemController.findAll()) {
            listItems.add(ItemMapper.map(a));
        }

        logger.debug("findAll() exit.");
        return Response.status(200).entity(listItems).build();
    }

    public Response createItem(ItemDTO entity) {
        try {
            this.ItemController.create(ItemMapper.map(entity));
        } catch (DAOException daoEx) {
                return Response.status(404).entity(new MessageDTO(daoEx.getMessage())).build();
        } catch (Exception ex) {
                return Response.status(500).entity(new MessageDTO(ex.getMessage())).build();
        }
        return Response.status(200).entity(new MessageDTO("Item has been updated.")).build();
    }

    public Response editItem(Integer id, ItemDTO entity) {
        try {
                entity.setId(id);
                this.ItemController.update(ItemMapper.map(entity));
        } catch (DAOException daoEx) {
                return Response.status(404).entity(new MessageDTO(daoEx.getMessage())).build();
        } catch (Exception ex) {
                return Response.status(500).entity(new MessageDTO(ex.getMessage())).build();
        }
        return Response.status(200).entity(new MessageDTO("Item has been updated.")).build();
    }

    public Response deleteItem(Integer id) {
        try {
                this.ItemController.delete(id);
        } catch (DAOException daoEx) {
                return Response.status(404).entity(new MessageDTO(daoEx.getMessage())).build();
        } catch (Exception ex) {
                return Response.status(500).entity(new MessageDTO(ex.getMessage())).build();
        }
        return Response.status(204).build();
    }

}
