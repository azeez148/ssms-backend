package com.ad.ssms.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ad.ssms.category.Category;

@CrossOrigin(origins = "http://localhost:4200") // Replace with your allowed origins
@RestController
@RequestMapping("/attributes")
public class AttributeController {

    @Autowired
    private AttributeService attributeService;

    @PostMapping("/addAttributes")
    public List<Attribute> addAttributes(@RequestBody List<AttributeRequest> attributeRequests) {
        List<Attribute> attributes = new ArrayList<>();
        for (AttributeRequest attributeRequest : attributeRequests) {
            Category category = attributeService.findCategoryById(attributeRequest.getCategoryId());
            if (category != null) {
                    Attribute attribute = new Attribute();
                    attribute.setCategory(category);
                    attribute.setName(attributeRequest.getName());
                    attribute.setDescription(attributeRequest.getDescription());
                    attribute.setValue(attributeRequest.getValue());
                    attributeService.saveAttribute(attribute);
                    attributes.add(attribute);
                
            }
        }
        return attributes;
    }

    @GetMapping("/all")
    public List<AttributeResponse> getAllAttributes() {
        List<Attribute> attributes = attributeService.findAllAttributes();

        Map<String, AttributeResponse> attributeResponseMap = new HashMap<>();
        for (Attribute attribute : attributes) {
            String name = attribute.getName();
            if (!attributeResponseMap.containsKey(name)) {
                AttributeResponse response = new AttributeResponse();
                response.setName(name);
                response.setDescription(attribute.getDescription());
                response.setValues(new ArrayList<>());
                attributeResponseMap.put(name, response);
            }
            attributeResponseMap.get(name).getValues().add(attribute.getValue());
        }

        return new ArrayList<>(attributeResponseMap.values());
    }
}
