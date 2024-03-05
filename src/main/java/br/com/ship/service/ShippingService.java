package br.com.ship.service;

import br.com.ship.dto.ShippingDTO;
import br.com.ship.entity.Shipping;
import br.com.ship.repository.ShippingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
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

        ProjectionOperation projectOperation = Aggregation.project("id", "attributes")
                .andExpression("objectToArray(attributes)").as("attributesArray");

        UnwindOperation unwindOperation = Aggregation.unwind("attributesArray");

        ProjectionOperation addFieldsOperation = Aggregation.project()
                .andInclude("id", "attributes", "attributesArray")
                .and(ConvertOperators.ToString.toString("$attributesArray.v")).as("stringValue");

        Criteria criteria = Criteria.where("stringValue").is(searchText);
        MatchOperation matchOperation = Aggregation.match(criteria);

        SkipOperation skipOperation = Aggregation.skip(pageable.getOffset());
        LimitOperation limitOperation = Aggregation.limit(pageable.getPageSize());

        Aggregation aggregation = Aggregation.newAggregation(
                projectOperation,
                unwindOperation,
                addFieldsOperation,
                matchOperation,
                skipOperation,
                limitOperation
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
