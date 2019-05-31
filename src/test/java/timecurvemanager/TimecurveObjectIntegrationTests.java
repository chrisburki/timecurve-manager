package timecurvemanager;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import timecurvemanager.domain.timecurveObject.TimecurveObject;
import timecurvemanager.domain.timecurveObject.TimecurveObjectRepository;
import timecurvemanager.domain.timecurveObject.TimecurveObjectValueType;
import timecurvemanager.infrastructure.persistence.timecurveObject.TimecurveObjectEntityRepository;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.Matchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TimecurveObjectIntegrationTests {

  @Autowired
  private WebApplicationContext applicationContext;

  @Autowired
  private TimecurveObjectRepository repository;

  @Autowired
  private TimecurveObjectEntityRepository entityRepository;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders
        .webAppContextSetup(applicationContext)
        .build();
    entityRepository.deleteAll();
  }

  @Test
  public void givenTimecurvesGetAll() throws Exception {

    TimecurveObject timecurveObject1 = new TimecurveObject(null, "T1", "L1", "NESN",
        TimecurveObjectValueType.CURRENCY, "CHF", null, true);
    repository.save(timecurveObject1);
    TimecurveObject timecurveObject2 = new TimecurveObject(null, "T2", "L2", "ROGN",
        TimecurveObjectValueType.CURRENCY, "CHF", null, false);
    repository.save(timecurveObject2);
    TimecurveObject timecurveObject3 = new TimecurveObject(null, "T3", "L3", "ABBN",
        TimecurveObjectValueType.CURRENCY, "CHF", null, true);
    repository.save(timecurveObject3);

    mockMvc.perform(get("/timecurve/objects")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$[0].name", is(timecurveObject1.getName())))
        .andExpect(jsonPath("$[1].name", is(timecurveObject2.getName())))
        .andExpect(jsonPath("$[2].name", is(timecurveObject3.getName())))
    ;


  }

  @Test
  public void givenTimecurveFindByNameAndLabel() {
    TimecurveObject timecurveObject = new TimecurveObject(null, "AAA",
        "REF:AAA-BUC;CCY:CHF;ASSET:NESN;DIM:STD", "AAA-BUC NESN CHF",
        TimecurveObjectValueType.CURRENCY, "CHF", "CHF", true);
    repository.save(timecurveObject);

    List<TimecurveObject> findByLastName = repository.findByName(timecurveObject.getName());

    assertThat(findByLastName).extracting(TimecurveObject::getName)
        .containsOnly(timecurveObject.getName());

    Optional<TimecurveObject> findOptionalByLabel = repository.findByTag(timecurveObject.getTag());

    assertThat(findOptionalByLabel.isPresent());
    assertThat(findOptionalByLabel.get().getTenantId()).contains(timecurveObject.getTenantId());
    assertThat(findOptionalByLabel.get().getTag()).contains(timecurveObject.getTag());
    assertThat(findOptionalByLabel.get().getName()).contains(timecurveObject.getName());

  }

}

