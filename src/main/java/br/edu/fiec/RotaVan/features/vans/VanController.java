package br.edu.fiec.RotaVan.features.vans;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/vans")
public class VanController {

    @GetMapping
    public void Hello (){
        System.out.println("Hello World");
    }
}
