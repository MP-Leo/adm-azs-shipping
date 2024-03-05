package br.com.ship.repository;


import br.com.ship.entity.Shipping;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingRepository extends MongoRepository<Shipping, String> {


}
