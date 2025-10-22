package dao;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import modelo.Emprestimo;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

/**
 *
 * @author José
 */
public class DaoEmprestimo {

    private final String URI = "mongodb://localhost:27017";
    private final String DATABASE = "frota";   // Dê o nome de sua preferência 
    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final String colecao;  // nome da coleção 
    private final MongoCollection<Emprestimo> collection;

    public DaoEmprestimo() {
        this.colecao = Emprestimo.class.getName();
        mongoClient = MongoClients.create(URI);
        CodecRegistry pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                org.bson.codecs.configuration.CodecRegistries.
                        fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        database = mongoClient.getDatabase(DATABASE).withCodecRegistry(pojoCodecRegistry);
        collection = database.getCollection(colecao, Emprestimo.class);
    }

//    public List<Emprestimo> buscarPorVeiculo(String placa) {
//        Bson filtro = Filters.eq("veiculo.placa", placa);
//        FindIterable<Emprestimo> resultsIterable = collection.find(filtro);
//
//        List<Emprestimo> retorno = new ArrayList();
//        resultsIterable.into(retorno);
//        return retorno;
//    }

    public List<Emprestimo> buscarPorPeriodo(String placa, 
            LocalDate retirada, 
            LocalDate devolucao) {
        Bson filtro1 = Filters.eq("veiculo.placa", placa);
        Bson filtro2 = Filters.gte("dataRetirada", retirada);
        Bson filtro3 = Filters.lte("dataDevolucao", devolucao);
        Bson filtros = Filters.and(filtro1, filtro2, filtro3);
        FindIterable<Emprestimo> resultados = collection.find(filtros);
        // converte em List/ArrayList        
        List<Emprestimo> retorno = new ArrayList();
        resultados.into(retorno);
        return retorno;
    }

}
