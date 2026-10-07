package dio.taskmanager.infrastructure.http;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;


@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
class TaskControllerTest {
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void should_save_and_retrieve_task_by_id() throws Exception {
        Map<String, String> taskRequest = new HashMap<>();
        taskRequest.put("title", "Aprender Spring RestDocs");
        taskRequest.put("description", "Ler o guia oficial do Spring");

        String responseJson = this.mockMvc.perform(
                        post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(taskRequest))
                )
                .andExpect(status().isCreated())
                .andDo(document("create-task",
                        requestFields(
                                fieldWithPath("title").description("Título da tarefa"),
                                fieldWithPath("description").description("Descrição detalhada")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Identificador Único da Tarefa"),
                                fieldWithPath("title").description("Título da Tarefa"),
                                fieldWithPath("description").description("Descrição detalhada"),
                                fieldWithPath("status").description("Status da Tarefa")
                        )
                ))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String generatedId = JsonPath.read(responseJson, "$.id");

        this.mockMvc.perform(
                        get("/tasks/{id}", generatedId)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(generatedId))
                .andExpect(jsonPath("$.title").value("Aprender Spring RestDocs"))
                .andDo(document("get-task-by-id",
                        pathParameters(
                                parameterWithName("id").description("Identificado único de Tarefas")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Identificador único da Tarefa"),
                                fieldWithPath("title").description("Título da Tarefa"),
                                fieldWithPath("description").description("Descrição Detalhada"),
                                fieldWithPath("status").description("Status da Tarefa")
                        )
                ));
    }
}