package exito.api;

import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Delete;
import net.serenitybdd.screenplay.rest.interactions.Get;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.serenitybdd.screenplay.rest.interactions.Put;
import net.serenitybdd.screenplay.rest.questions.ResponseConsequence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.screenplay.annotations.CastMember;

@ExtendWith(SerenityJUnit5Extension.class)
class ApiTest {

    @CastMember(name = "Wendy")
    Actor wendy;
    
    @Test
    void searchBySingleKeyword() {
    	
    	 Actor actor = Actor.named("Sam the supervisor")
                 .whoCan(CallAnApi.at("https://dummy.restapiexample.com/api/v1"));
    	    	
        // Crear un empleado
        String employeeData = "{\"name\":\"Cristian David\",\"salary\":\"20000\",\"age\":\"31\"}";
        actor.attemptsTo(
            Post.to("/create")
                .with(request -> request.contentType(ContentType.JSON).body(employeeData))
        );
        
        actor.should(
            ResponseConsequence.seeThatResponse("Employee is created successfully",
                response -> response.statusCode(200))
        );

        System.out.println("creado");
        
        Response respo = SerenityRest.lastResponse();
        
        System.out.println(respo.getBody().asPrettyString());
        
        int id = respo.getBody().jsonPath().getInt("data.id");
        System.out.println("ID extraído: " + id);
        
                
        // Consultar el empleado creado
        actor.attemptsTo(
            Get.resource("/employee/"+id+"")
        );
        
        respo = SerenityRest.lastResponse();
        
        System.out.println(respo.getBody().asPrettyString());
        
        actor.should(
            ResponseConsequence.seeThatResponse("Employee information is retrieved successfully",
                response -> response.statusCode(200))
        );

       
        // Actualizar la información del empleado
        String updatedEmployeeData = "{\"name\":\"John Doe\",\"salary\":\"35000\",\"age\":\"30\"}";
        actor.attemptsTo(
            Put.to("/update/"+id+"")
                .with(request -> request.contentType(ContentType.JSON).body(updatedEmployeeData))
        );

        actor.should(
            ResponseConsequence.seeThatResponse("Employee information is updated successfully",
                response -> response.statusCode(200))
        );

        System.out.println("actualizado");
       
        // Borrar al empleado
        actor.attemptsTo(
            Delete.from("/delete/"+id+"")
        );

        actor.should(
            ResponseConsequence.seeThatResponse("Employee is deleted successfully",
                response -> response.statusCode(200))
        );
    	
        System.out.println("eliminado");
    	
    }
    
}
