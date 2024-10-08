package tacs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationDTO {
    private String name;
    private double price;
    private int quantityTickets;
}
