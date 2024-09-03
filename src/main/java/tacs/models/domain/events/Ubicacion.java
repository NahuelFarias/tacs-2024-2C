package tacs.models.domain.events;

import java.util.Objects;
import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Basic
    public String nombre;
    public double precio;

    public Ubicacion(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    // Sobrescribir equals() para comparar objetos basados en los valores de sus atributos
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Verifica si ambos objetos tienen la misma referencia
        if (o == null || getClass() != o.getClass()) return false; // Verifica si el objeto comparado es null o de diferente clase

        Ubicacion ubicacion = (Ubicacion) o; // Realiza un casting del objeto comparado a la clase Persona

        // Compara todos los atributos de la clase para determinar si son iguales
        return Objects.equals(nombre, ubicacion.nombre) &&
                Objects.equals(precio, ubicacion.precio);
    }

    // Sobrescribir hashCode() para garantizar que objetos iguales tengan el mismo hash
    @Override
    public int hashCode() {
        return Objects.hash(nombre, precio);
    }
}
