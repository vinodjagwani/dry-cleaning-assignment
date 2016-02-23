package com.drycleaning.assignment;


import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Vinod on 01/29/2015.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(profiles = "test")
@SpringApplicationConfiguration(classes = DryCleanGuarantyTimeApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class AbstractDryCleanGuarantyTimeTest {
}
