package com.baozistore.api;

import com.baozistore.api.repository.ClienteRepository;
import com.baozistore.api.repository.PedidoRepository;
import com.baozistore.api.repository.ProdutoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AssignmentApiIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @BeforeEach
    void limparBanco() {
        pedidoRepository.deleteAll();
        produtoRepository.deleteAll();
        clienteRepository.deleteAll();
    }

    @Test
    void createsListsReadsAndDeletesAllAssignmentEntities() throws Exception {
        JsonNode cliente = postAndRead("/api/clientes", """
                {
                  "nome": "Ana Silva",
                  "clienteDesde": "2026-10-12"
                }
                """);
        long clienteId = cliente.get("id").asLong();
        assertEquals(Set.of("id", "nome", "clienteDesde"), fieldNames(cliente));
        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(clienteId))
                .andExpect(jsonPath("$[0].clienteDesde").value("2026-10-12"));
        mockMvc.perform(get("/api/clientes/{id}", clienteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Ana Silva"));

        JsonNode produto = postAndRead("/api/produtos", """
                {
                  "nome": "Baozi de porco",
                  "preco": 12.50,
                  "estoque": true
                }
                """);
        long produtoId = produto.get("id").asLong();
        assertEquals(Set.of("id", "nome", "preco", "estoque"), fieldNames(produto));
        assertTrue(produto.get("estoque").isBoolean());
        mockMvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(produtoId))
                .andExpect(jsonPath("$[0].estoque").value(true));
        mockMvc.perform(get("/api/produtos/{id}", produtoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.preco").value(12.5));

        JsonNode pedido = postAndRead("/api/pedidos", """
                {
                  "clienteId": %d,
                  "produtoId": %d,
                  "quantidade": 2
                }
                """.formatted(clienteId, produtoId));
        long pedidoId = pedido.get("id").asLong();
        assertEquals(Set.of("id", "clienteId", "produtoId", "quantidade"), fieldNames(pedido));
        assertEquals(clienteId, pedido.get("clienteId").asLong());
        assertEquals(produtoId, pedido.get("produtoId").asLong());
        assertEquals(2, pedido.get("quantidade").asInt());
        assertFalse(pedido.has("produtoIds"));
        assertFalse(pedido.has("status"));
        mockMvc.perform(get("/api/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clienteId").value(clienteId))
                .andExpect(jsonPath("$[0].produtoId").value(produtoId))
                .andExpect(jsonPath("$[0].quantidade").value(2));
        mockMvc.perform(get("/api/pedidos/{id}", pedidoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pedidoId));

        mockMvc.perform(delete("/api/pedidos/{id}", pedidoId)).andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/produtos/{id}", produtoId)).andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/clientes/{id}", clienteId)).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/clientes")).andExpect(status().isOk()).andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/api/produtos")).andExpect(status().isOk()).andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/api/pedidos")).andExpect(status().isOk()).andExpect(jsonPath("$").isEmpty());
    }

    private JsonNode postAndRead(String path, String body) throws Exception {
        MvcResult result = mockMvc.perform(post(path)
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString());
    }

    private Set<String> fieldNames(JsonNode node) {
        return StreamSupport.stream(((Iterable<String>) () -> node.fieldNames()).spliterator(), false)
                .collect(HashSet::new, Set::add, Set::addAll);
    }
}
