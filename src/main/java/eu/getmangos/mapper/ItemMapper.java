package eu.getmangos.mapper;

import java.util.Date;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import eu.getmangos.entities.Item;
import eu.getmangos.dto.ItemDTO;

@Mapper(componentModel = "cdi")
public interface ItemMapper {

    ItemDTO map(Item entity);

    Item map(ItemDTO dto);

    default Date map(Long value) {
        return new Date(value);
    }

    default Long map(Date date) {
        return date.getTime();
    }
}
