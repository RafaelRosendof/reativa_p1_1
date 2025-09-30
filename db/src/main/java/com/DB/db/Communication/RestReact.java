package com.DB.db.Communication;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/db")
public class RestReact {
    

    


    @GetMapping("/isAlive")
    public String TaVivo() {
        return "Estou Vivo!";
    }
    
}