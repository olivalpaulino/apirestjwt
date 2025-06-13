package dobackaofront.apirestjwt.repository;

import dobackaofront.apirestjwt.dominio.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    // Métodos CRUD já são herdados automaticamente
}
