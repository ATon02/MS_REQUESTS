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
import io.swagger.v3.oas.models.parameters.PathParameter;
import io.swagger.v3.oas.models.parameters.QueryParameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

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
import co.com.powerup.usecase.requestclient.dto.ResponseDataRequest;
import co.com.powerup.usecase.requestclient.dto.ResponseDataTotal;

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
            openApi.getComponents()
                    .addSecuritySchemes("bearerAuth",
                            new SecurityScheme()
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .bearerFormat("JWT"));
            openApi.addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
            // REQUEST TYPE
            PathItem requestTypePath = new PathItem()
                    .get(new Operation()
                            .operationId("findRequestTypes")
                            .tags(List.of("RequestType"))
                            .summary("Obtiene todos los tipos de solicitudes")
                            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("Lista de tipos de solicitudes")
                                            .content(new Content()
                                                    .addMediaType("application/json",
                                                            new io.swagger.v3.oas.models.media.MediaType()
                                                                    .schema(new ArraySchema().items(
                                                                            new Schema<>().$ref(
                                                                                    "#/components/schemas/RequestTypeResponse"))))))))
                    .post(new Operation()
                            .operationId("saveRequestType")
                            .tags(List.of("RequestType"))
                            .summary("Crea un nuevo tipo de solicitud")
                            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                            .requestBody(new RequestBody()
                                    .description("DTO para crear un tipo de solicitud")
                                    .required(true)
                                    .content(new Content()
                                            .addMediaType("application/json",
                                                    new io.swagger.v3.oas.models.media.MediaType()
                                                            .schema(new Schema<>().$ref(
                                                                    "#/components/schemas/RequestTypeCreateDTO")))))
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("Tipo de solicitud creado")
                                            .content(new Content()
                                                    .addMediaType("application/json",
                                                            new io.swagger.v3.oas.models.media.MediaType()
                                                                    .schema(new Schema<>().$ref(
                                                                            "#/components/schemas/RequestTypeResponse")))))));

            openApi.path("/api/v1/request-type", requestTypePath);
            // REQUEST CLIENT
            PathItem requestClientPath = new PathItem()
                    .get(new Operation()
                            .operationId("findRequestsFilter")
                            .tags(List.of("RequestClient"))
                            .summary(
                                    "Obtiene todas las solicitudes de clientes filtradas por estado (1)Pendiente por revisión,(3)Rechazada,(5)Revision manual y paginadas")
                            .description(
                                    "Devuelve una lista paginada de solicitudes de clientes según los filtros proporcionados.")
                            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                            .addParametersItem(new QueryParameter()
                                    .name("page")
                                    .description("Número de página (1 por defecto)")
                                    .schema(new IntegerSchema()._default(1)))
                            .addParametersItem(new QueryParameter()
                                    .name("size")
                                    .description("Cantidad de registros por página (10 por defecto)")
                                    .schema(new IntegerSchema()._default(10)))
                            .addParametersItem(new QueryParameter()
                                    .name("status")
                                    .description("Lista de IDs de estados para filtrar, separados por coma")
                                    .schema(new StringSchema()))
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("Lista de solicitudes de clientes")
                                            .content(new Content()
                                                    .addMediaType("application/json",
                                                            new io.swagger.v3.oas.models.media.MediaType()
                                                                    .schema(new ArraySchema()
                                                                            .items(new Schema<>().$ref(
                                                                                    "#/components/schemas/ResponseDataRequest"))))))))
                    .post(new Operation()
                            .operationId("saveRequest")
                            .tags(List.of("RequestClient"))
                            .summary("Crea una nueva solicitud de cliente")
                            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                            .requestBody(new RequestBody()
                                    .description("DTO para crear una solicitud de cliente")
                                    .required(true)
                                    .content(new Content()
                                            .addMediaType("application/json",
                                                    new io.swagger.v3.oas.models.media.MediaType()
                                                            .schema(new Schema<>().$ref(
                                                                    "#/components/schemas/RequestClientCreateDTO")))))
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("Solicitud de cliente creada")
                                            .content(new Content()
                                                    .addMediaType("application/json",
                                                            new io.swagger.v3.oas.models.media.MediaType()
                                                                    .schema(new Schema<>().$ref(
                                                                            "#/components/schemas/RequestClientResponse")))))));
            openApi.path("/api/v1/request", requestClientPath);
            PathItem updateRequest = new PathItem()
                    .put(new Operation()
                            .operationId("updateStatusRequest")
                            .tags(List.of("RequestClient"))
                            .summary("Actualiza el estado de una solicitud de cliente")
                            .description(
                                    "Actualiza el estado de una solicitud según el ID proporcionado. Requiere role 'asesor'.")
                            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                            .addParametersItem(new PathParameter()
                                    .name("id")
                                    .description("ID de la solicitud a actualizar")
                                    .required(true)
                                    .schema(new IntegerSchema()))
                            .addParametersItem(new QueryParameter()
                                    .name("statusId")
                                    .description("ID del nuevo estado a asignar")
                                    .required(true)
                                    .schema(new IntegerSchema()))
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("Solicitud de cliente actualizada correctamente")
                                            .content(new Content()
                                                    .addMediaType("application/json",
                                                            new io.swagger.v3.oas.models.media.MediaType()
                                                                    .schema(new Schema<>().$ref(
                                                                            "#/components/schemas/RequestClientResponse")))))));
            openApi.path("/api/v1/request/{id}", updateRequest);
            PathItem requestClientPathAll = new PathItem()
                    .get(new Operation()
                            .operationId("findRequests")
                            .tags(List.of("RequestClient"))
                            .summary("Obtiene todos las solicitudes de clientes")
                            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("Lista de solicitudes de clientes")
                                            .content(new Content()
                                                    .addMediaType("application/json",
                                                            new io.swagger.v3.oas.models.media.MediaType()
                                                                    .schema(new ArraySchema().items(
                                                                            new Schema<>().$ref(
                                                                                    "#/components/schemas/RequestClientResponse"))))))));
            openApi.path("/api/v1/request/all", requestClientPathAll);
            PathItem requestTotal = new PathItem()
                    .get(new Operation()
                            .operationId("totalRequestsByStatus")
                            .tags(List.of("RequestClient"))
                            .summary("Obtiene el totalizado de solicitudes en un estado ya sea en cantidad o monto")
                            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                            .addParametersItem(new QueryParameter()
                                    .name("type")
                                    .description("Tipo de total a calcular (APPROVED_REQUESTS o APPROVED_AMOUNT)")
                                    .required(true)
                                    .schema(new StringSchema()
                                        ._enum(List.of("APPROVED_REQUESTS", "APPROVED_AMOUNT"))))
                            .addParametersItem(new QueryParameter()
                                    .name("statusId")
                                    .description("ID del estado a buscar")
                                    .required(true)
                                    .schema(new IntegerSchema()))
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("Totales de solicitudes de clientes")
                                            .content(new Content()
                                                    .addMediaType("application/json",
                                                            new io.swagger.v3.oas.models.media.MediaType()
                                                                    .schema(new ArraySchema().items(
                                                                            new Schema<>().$ref(
                                                                                    "#/components/schemas/ResponseDataTotal"))))))));
            openApi.path("/api/v1/request/total", requestTotal);
            // REQUEST STATUS
            PathItem requestStatusPath = new PathItem()
                    .get(new Operation()
                            .operationId("findRequestStatuses")
                            .tags(List.of("RequestStatus"))
                            .summary("Obtiene todos los estados de solicitudes")
                            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("Lista de estados de solicitudes")
                                            .content(new Content()
                                                    .addMediaType("application/json",
                                                            new io.swagger.v3.oas.models.media.MediaType()
                                                                    .schema(new ArraySchema().items(
                                                                            new Schema<>().$ref(
                                                                                    "#/components/schemas/RequestStatusResponse"))))))))
                    .post(new Operation()
                            .operationId("saveRequestStatus")
                            .tags(List.of("RequestStatus"))
                            .summary("Crea un nuevo estado de solicitud")
                            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                            .requestBody(new RequestBody()
                                    .description("DTO para crear un estado de solicitud")
                                    .required(true)
                                    .content(new Content()
                                            .addMediaType("application/json",
                                                    new io.swagger.v3.oas.models.media.MediaType()
                                                            .schema(new Schema<>().$ref(
                                                                    "#/components/schemas/RequestStatusCreateDTO")))))
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("Estado de request solicitud")
                                            .content(new Content()
                                                    .addMediaType("application/json",
                                                            new io.swagger.v3.oas.models.media.MediaType()
                                                                    .schema(new Schema<>().$ref(
                                                                            "#/components/schemas/RequestStatusResponse")))))));

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
                            .addProperty("timestamp", new StringSchema().format("date-time")))
                    .addSchemas("ResponseDataRequest", new Schema<ResponseDataRequest>()
                            .addProperty("id", new IntegerSchema())
                            .addProperty("amount", new NumberSchema())
                            .addProperty("deadline", new IntegerSchema())
                            .addProperty("email", new StringSchema())
                            .addProperty("name", new StringSchema())
                            .addProperty("requestType", new StringSchema())
                            .addProperty("requestStatus", new StringSchema())
                            .addProperty("baseSalary", new NumberSchema().format("double"))
                            .addProperty("totalMonthlyDebt", new NumberSchema().format("double"))
                            .addProperty("interestRate", new NumberSchema().format("double")))
                    .addSchemas("ResponseDataTotal", new Schema<ResponseDataTotal>()
                            .addProperty("status", new StringSchema())
                            .addProperty("type", new StringSchema())
                            .addProperty("value", new NumberSchema().format("double")));
        };
    }

}
