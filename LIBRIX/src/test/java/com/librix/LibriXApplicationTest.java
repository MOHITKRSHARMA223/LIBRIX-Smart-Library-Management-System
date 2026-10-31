package com.librix;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:librix-test;DB_CLOSE_DELAY=-1",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class LibriXApplicationTest {
    @Autowired MockMvc mockMvc;

    @Test void loginPageIsPublic() throws Exception {
            mockMvc.perform(get("/student/login")).andExpect(status().isOk()).andExpect(view().name("auth/student-login"));
            mockMvc.perform(get("/admin/login")).andExpect(status().isOk()).andExpect(view().name("auth/admin-login"));
    }

    @Test void dashboardIsSecuredAndRenders() throws Exception {
        mockMvc.perform(get("/dashboard").with(user("admin").roles("SUPER_ADMIN"))).andExpect(status().isOk()).andExpect(view().name("dashboard/index"));
    }

    @Test void dashboardRedirectsAnonymousUsers() throws Exception {
        mockMvc.perform(get("/dashboard")).andExpect(status().is3xxRedirection());
    }

    @Test void catalogAndSearchPagesRenderForLibrarians() throws Exception {
        mockMvc.perform(get("/categories").with(user("librarian").roles("LIBRARIAN")))
            .andExpect(status().isOk()).andExpect(view().name("catalog/list"));
        mockMvc.perform(get("/search").with(user("librarian").roles("LIBRARIAN")))
            .andExpect(status().isOk()).andExpect(view().name("search/index"));
    }

    @Test void activityPageRequiresSuperAdmin() throws Exception {
        mockMvc.perform(get("/admin/activity").with(user("librarian").roles("LIBRARIAN")))
            .andExpect(status().isForbidden());
        mockMvc.perform(get("/admin/activity").with(user("admin").roles("SUPER_ADMIN")))
            .andExpect(status().isOk()).andExpect(view().name("admin/activity"));
    }

        @Test void portalsHaveSeparateDashboardsAndPermissions() throws Exception {
            mockMvc.perform(get("/student/dashboard").with(user("student").roles("STUDENT")))
                .andExpect(status().isOk()).andExpect(view().name("student/dashboard"));
            mockMvc.perform(get("/admin/dashboard").with(user("admin").roles("SUPER_ADMIN")))
                .andExpect(status().isOk()).andExpect(view().name("admin/dashboard"));
            mockMvc.perform(get("/admin/dashboard").with(user("student").roles("STUDENT")))
                .andExpect(status().isForbidden());
            mockMvc.perform(get("/student/dashboard").with(user("admin").roles("SUPER_ADMIN")))
                .andExpect(status().isForbidden());
        }
}
