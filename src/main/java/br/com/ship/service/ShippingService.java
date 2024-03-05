package br.com.ship.service;

import br.com.ship.dto.ShippingDTO;
import br.com.ship.entity.Shipping;
import br.com.ship.repository.ShippingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ShippingService {

    @Autowired
    private ShippingRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;


    public Page<Shipping> findAllShippings(Pageable pageable){

        return repository.findAll(pageable);
    }

    public Shipping findShippingById(String id){

        return repository.findById(id).get();
    }

    public Page<Shipping> findByAnyAttribute(String searchText, Pageable pageable) {

        Criteria searchCriteria = Criteria.where("attributes.$values").is(searchText);

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.project("id", "attributes")
                        .andExpression("objectToArray(attributes)").as("attributesArray"),
                Aggregation.unwind("attributesArray"),
                Aggregation.match(Criteria.where("attributesArray.v").is(searchText)),
                Aggregation.skip(pageable.getOffset()),
                Aggregation.limit(pageable.getPageSize())
        );

        AggregationResults<Shipping> results = mongoTemplate.aggregate(aggregation, "shipping", Shipping.class);
        List<Shipping> shippingList = results.getMappedResults();

        Query countQuery = new Query(searchCriteria);

        long total = mongoTemplate.count(countQuery, Shipping.class);

        return new PageImpl<>(shippingList, pageable, total);
    }


    public Shipping createShipping(ShippingDTO dto) {
        Shipping shipping = Shipping.builder()
                .attributes(dto.getAttributes())
                .build();

        return repository.save(shipping);
    }

    public Shipping updateShipping(String id, ShippingDTO dto){

        Optional<Shipping> optional = repository.findById(id);

        if (optional.isPresent()){

            Shipping shipping = optional.get();

            Map<String, Object> existingAttributes = shipping.getAttributes();

            existingAttributes.putAll(dto.getAttributes());

            return repository.save(shipping);

        }

        return null;

    }

    public Shipping deleteShipping(String id){

        Optional<Shipping> optional = repository.findById(id);

        if (optional.isPresent()){

            repository.deleteById(id);

            return optional.get();
        }

        return null;
    }

}
