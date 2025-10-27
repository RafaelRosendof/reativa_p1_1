package com.ms1Service.ms1.DbPart;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Service;

@Service

public interface dbService {
    

    public String saveRedisData(@Argument String symbol);

    public String getTop1Stock();

    public String getTop2Stock();

    // atenção nesse método -> falta implementar a lógica de avisar o ms2 que já deu de requisições
    public String stopRedis();
    // este método, vai ser o seguinte -> ele vai avisar ao ms2 que já deu de requisições e o ms2 vai passar tudo do redis para o postgres ( somente o top 2 )
}