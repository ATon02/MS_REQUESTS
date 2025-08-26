package co.com.powerup.api.config;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

import java.util.List;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import co.com.powerup.api.dtos.request.RequestClientCreateDTO;
import co.com.powerup.api.dtos.request.RequestStatusCreateDTO;
import co.com.powerup.api.dtos.request.RequestTypeCreateDTO;
import co.com.powerup.api.dtos.response.ErrorResponse;
import co.com.powerup.api.dtos.response.RequestClientResponse;
import co.com.powerup.api.dtos.response.RequestStatusResponse;
import co.com.powerup.api.dtos.response.RequestTypeResponse;



@Configuration
public class OpenApiConfig {
    @Bean
    public GroupedOpenApi publicApi(OpenApiCustomizer customizer) {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/api/v1/**")
                .addOpenApiCustomizer(customizer)
                .build();
    }

    @Bean
    @Primary
    public OpenApiCustomizer customizer() {
        return openApi -> {

            PathItem requestTypePath = new PathItem()
                    .get(new Operation()
                            .operationId("findRequestTypes")
                            .tags(List.of("RequestType"))
                            .summary("Obtiene todos los tipos de solicitudes")
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("Lista de tipos de solicitudes")
                                            .content(new Content()
                                                    .addMediaType("application/json",
                                                            new io.swagger.v3.oas.models.media.MediaType()
                                                                    .schema(new ArraySchema().items(
                                                                            new Schema<>().$ref("#/components/schemas/RequestTypeResponse")
                                                                    ))
                                                    )
                                            )
                                    )
                            )
                    )
                    .post(new Operation()
                            .operationId("saveRequestType")
                            .tags(List.of("RequestType"))
                            .summary("Crea un nuevo tipo de solicitud")
                            .requestBody(new RequestBody()
                                    .description("DTO para crear un tipo de solicitud")
                                    .required(true)
                                    .content(new Content()
                                            .addMediaType("application/json",
                                                    new io.swagger.v3.oas.models.media.MediaType()
                                                            .schema(new Schema<>().$ref("#/components/schemas/RequestTypeCreateDTO")))))
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("Tipo de solicitud creado")
                                            .content(new Content()
                                                    .addMediaType("application/json",
                                                            new io.swagger.v3.oas.models.media.MediaType()
                                                                    .schema(new Schema<>().$ref("#/components/schemas/RequestTypeResponse")))))));

            openApi.path("/api/v1/request-type", requestTypePath);

            PathItem requestClientPath = new PathItem()
                    .get(new Operation()
                            .operationId("findRequests")
                            .tags(List.of("RequestClient"))
                            .summary("Obtiene todos las solicitudes de clientes")
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("Lista de solicitudes de clientes")
                                            .content(new Content()
                                                    .addMediaType("application/json",
                                                            new io.swagger.v3.oas.models.media.MediaType()
                                                                    .schema(new ArraySchema().items(
                                                                            new Schema<>().$ref("#/components/schemas/RequestClientResponse")
                                                                    ))
                                                    )
                                            )
                                    )
                            )
                    )
                    .post(new Operation()
                            .operationId("saveRequest")
                            .tags(List.of("RequestClient"))
                            .summary("Crea una nueva solicitud de cliente")
                            .requestBody(new RequestBody()
                                    .description("DTO para crear una solicitud de cliente")
                                    .required(true)
                                    .content(new Content()
                                            .addMediaType("application/json",
                                                    new io.swagger.v3.oas.models.media.MediaType()
                                                            .schema(new Schema<>().$ref("#/components/schemas/RequestClientCreateDTO")))))
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("Solicitud de cliente creada")
                                            .content(new Content()
                                                    .addMediaType("application/json",
                                                            new io.swagger.v3.oas.models.media.MediaType()
                                                                    .schema(new Schema<>().$ref("#/components/schemas/RequestClientResponse")))))));

            openApi.path("/api/v1/request", requestClientPath);

            PathItem requestStatusPath = new PathItem()
                    .get(new Operation()
                            .operationId("findRequestStatuses")
                            .tags(List.of("RequestStatus"))
                            .summary("Obtiene todos los estados de solicitudes")
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("Lista de estados de solicitudes")
                                            .content(new Content()
                                                    .addMediaType("application/json",
                                                            new io.swagger.v3.oas.models.media.MediaType()
                                                                    .schema(new ArraySchema().items(
                                                                            new Schema<>().$ref("#/components/schemas/RequestStatusResponse")
                                                                    ))
                                                    )
                                            )
                                    )
                            )
                    )
                    .post(new Operation()
                            .operationId("saveRequestStatus")
                            .tags(List.of("RequestStatus"))
                            .summary("Crea un nuevo estado de solicitud")
                            .requestBody(new RequestBody()
                                    .description("DTO para crear un estado de solicitud")
                                    .required(true)
                                    .content(new Content()
                                            .addMediaType("application/json",
                                                    new io.swagger.v3.oas.models.media.MediaType()
                                                            .schema(new Schema<>().$ref("#/components/schemas/RequestStatusCreateDTO")))))
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("Estado de request solicitud")
                                            .content(new Content()
                                                    .addMediaType("application/json",
                                                            new io.swagger.v3.oas.models.media.MediaType()
                                                                    .schema(new Schema<>().$ref("#/components/schemas/RequestStatusResponse")))))));

            openApi.path("/api/v1/status-request", requestStatusPath);

            openApi.getComponents()
                    .addSchemas("RequestTypeCreateDTO", new Schema<RequestTypeCreateDTO>()
                            .addProperty("name", new StringSchema())
                            .addProperty("minAmount", new NumberSchema().format("double"))
                            .addProperty("maxAmount", new NumberSchema().format("double"))
                            .addProperty("interestRate", new NumberSchema().format("double"))
                            .addProperty("automaticValidation", new BooleanSchema()))
                    .addSchemas("RequestTypeResponse", new Schema<RequestTypeResponse>()
                            .addProperty("id", new IntegerSchema().format("int64"))
                            .addProperty("name", new StringSchema())
                            .addProperty("minAmount", new NumberSchema().format("double"))
                            .addProperty("maxAmount", new NumberSchema().format("double"))
                            .addProperty("interestRate", new NumberSchema().format("double"))
                            .addProperty("automaticValidation", new BooleanSchema()))
                    .addSchemas("RequestClientCreateDTO", new Schema<RequestClientCreateDTO>()
                            .addProperty("amount", new NumberSchema().format("double"))
                            .addProperty("deadline", new IntegerSchema().format("int64"))
                            .addProperty("email", new StringSchema())
                            .addProperty("requestTypeId", new IntegerSchema().format("int64"))
                            .addProperty("identityDocument", new StringSchema()))
                    .addSchemas("RequestClientResponse", new Schema<RequestClientResponse>()
                            .addProperty("id", new IntegerSchema().format("int64"))
                            .addProperty("amount", new NumberSchema().format("double"))
                            .addProperty("deadline", new IntegerSchema().format("int64"))
                            .addProperty("email", new StringSchema())
                            .addProperty("requestTypeId", new IntegerSchema().format("int64"))
                            .addProperty("statusId", new IntegerSchema().format("int64")))
                    .addSchemas("RequestStatusCreateDTO", new Schema<RequestStatusCreateDTO>()
                            .addProperty("name", new StringSchema())
                            .addProperty("description", new StringSchema()))
                    .addSchemas("RequestStatusResponse", new Schema<RequestStatusResponse>()
                            .addProperty("id", new IntegerSchema().format("int64"))
                            .addProperty("name", new StringSchema())
                            .addProperty("description", new StringSchema()))
                    .addSchemas("ErrorResponse", new Schema<ErrorResponse>()
                            .addProperty("status", new IntegerSchema().format("int32"))
                            .addProperty("message", new StringSchema())
                            .addProperty("path", new StringSchema())
                            .addProperty("timestamp", new StringSchema().format("date-time")));
        };
    }


    
}


