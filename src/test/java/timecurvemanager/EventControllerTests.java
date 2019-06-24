package timecurvemanager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class EventControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void givenDimensionGetAllEvents() throws Exception {
    String uri = new String("/timecurve/events?");
    uri = uri + "dimension=SUBLEDGER";
    mockMvc.perform(get(uri)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
    //   .andExpect(jsonPath("$", hasSize(1)))
    ;
  }
}
