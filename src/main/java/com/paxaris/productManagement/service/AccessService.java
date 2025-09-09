package com.paxaris.productManagement.service;

import com.paxaris.productManagement.dto.ProductAccessRequest;
import com.paxaris.productManagement.dto.ProductAccessResponse;

public interface AccessService {
    ProductAccessResponse checkAccess(ProductAccessRequest request);
}
