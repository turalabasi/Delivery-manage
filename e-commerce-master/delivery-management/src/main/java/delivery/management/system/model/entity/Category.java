package delivery.management.system.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;

    @ManyToMany(mappedBy = "categories",fetch = FetchType.LAZY)
    private List<Product> products;
    @Builder.Default
    private boolean status = true;

    public Category(String electronics, boolean b) {
    }
}
