package com.telusko.ecom_proj.controller;

import com.telusko.ecom_proj.model.Product;
import com.telusko.ecom_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController
{
    @Autowired
    private ProductService service;

    @RequestMapping("/")
    public String greet()
    {
        return "Hello from greet home_page";
    }
    @GetMapping("/products")
     public ResponseEntity<List<Product>> getAllProducts()
     {
//         System.out.println(service.getAllProducts());
         return new ResponseEntity<>(service.getAllProducts(),HttpStatus.OK) ;
     }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductsById(@PathVariable int id)
    {
        Product prod= service.getProductsById(id);
       if(prod!=null)
       {
           return new ResponseEntity<>(prod, HttpStatus.OK);
       }
       else {
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }

    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile)
    {
        try
        {System.out.println(product);
            Product product1=service.addProduct(product,imageFile);
            return new ResponseEntity<>(product1,HttpStatus.OK);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("product/{productId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId)
    {
        Product product=service.getProductsById(productId);
        byte[] imageFile = product.getImageData();

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(product.getImageType()))
                .body(imageFile);
    }

    @PutMapping("product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id,@RequestPart  Product product,@RequestPart MultipartFile imageFile)
    {
        Product product1 = null;
        try {
            product1 = service.updateProduct(id,product,imageFile);
        }
        catch (IOException e)
        {
            return  new ResponseEntity<>("Failed to Update",HttpStatus.BAD_REQUEST);
        }
        if(product1!=null)
            return  new ResponseEntity<>("Updated",HttpStatus.OK);
        else
            return  new ResponseEntity<>("Failed to Update",HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<String>deleteProduct(@PathVariable int id)
    {
        Product product=service.getProductsById(id);
        if(product!=null)
        {
            service.deleteProduct(id);
            return new ResponseEntity<>("Deleted",HttpStatus.OK);
        }
        else
        {
            return  new ResponseEntity<>("Product Not Found",HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>>searchProducts(@RequestParam String keyword)
    {
        System.out.println("Searching With : "+keyword);
        List<Product> products= service.searchProducts(keyword);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }

}
