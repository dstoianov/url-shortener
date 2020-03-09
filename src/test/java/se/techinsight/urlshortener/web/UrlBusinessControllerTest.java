package se.techinsight.urlshortener.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import se.techinsight.urlshortener.api.dto.OriginalUrlDto;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"test"})
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/insert_test_data.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:sql/delete_test_data.sql")
class UrlBusinessControllerTest {

    @Autowired
    private MockMvc mockMvc;

//    @Autowired
//    private ObjectMapper objectMapper;

    @Test
    void getIndexTest() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andDo(print());
    }

    @Test
    void getExistedLink_RedirectToLinkFromDb() throws Exception {
        this.mockMvc.perform(get("/g2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(containsString("redirect:https://www.google.com.ua")))
                .andDo(print());
    }

    @Test
    void getNotExistedLink_NOT_FOUND() throws Exception {
        this.mockMvc.perform(get("/zzzz"))
                .andExpect(status().isOk())
                .andExpect(view().name("not_found"))
                .andDo(print());
    }


    @Test
    void createLinkWithKey_ResultPage() throws Exception {
        /*
        OriginalUrlDto payload = new OriginalUrlDto();
        payload.setLongUrl("https://www.yahoo.com/");
        payload.setShortKey("");

        this.mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(payload))
//                .accept(MediaType.APPLICATION_JSON_VALUE))
        ).andExpect(status().isOk())
                .andDo(print());
*/

        this.mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("longUrl", "https://www.yahoo.com/")
                .param("shortKey", "go-link")
                .sessionAttr("originalUrlDto", new OriginalUrlDto())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("result"))
                .andExpect(model().attribute("data", hasProperty("shortKey", containsString("go-link"))))
                .andDo(print());
    }

    @Test
    void createLinkWithExistedKey_RedirectPage() throws Exception {
        this.mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("longUrl", "https://www.yahoo.com/")
                .param("shortKey", "g3")
                .sessionAttr("originalUrlDto", new OriginalUrlDto())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(flash().attribute("message", containsString("Key 'g3' already exists")))
                .andDo(print());
    }

    @Test
    void createLinkWithError_IndexPage() throws Exception {
        this.mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("longUrl", "invalid_url")
                .param("shortKey", "/")
                .sessionAttr("originalUrlDto", new OriginalUrlDto())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeHasFieldErrors("originalUrlDto", "longUrl"))
                .andDo(print());
    }

}