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
            mockMvc.perform(get("/student/register")).andExpect(status().isOk()).andExpect(view().name("auth/student-register"));
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

        @Test void newOperationalRoutesRespectPortalRoles() throws Exception {
            mockMvc.perform(get("/admin/reports").with(user("admin").roles("SUPER_ADMIN")))
                .andExpect(status().isOk()).andExpect(view().name("admin/reports"));
            mockMvc.perform(get("/admin/reservations").with(user("admin").roles("SUPER_ADMIN")))
                .andExpect(status().isOk()).andExpect(view().name("admin/reservations"));
            mockMvc.perform(get("/admin/reports").with(user("student").roles("STUDENT")))
                .andExpect(status().isForbidden());
            mockMvc.perform(get("/borrowing").with(user("student").roles("STUDENT")))
                .andExpect(status().isForbidden());
        }

        @Test void studentLibraryPageRendersReservationSection() throws Exception {
            mockMvc.perform(get("/student/my-books").with(user("student").roles("STUDENT")))
                .andExpect(status().isOk()).andExpect(view().name("student/my-books"));
        }

        @Test void studentToolsAndAdminAccountsAreRoleProtected() throws Exception {
            mockMvc.perform(get("/student/resources").with(user("student").roles("STUDENT")))
                .andExpect(status().isOk()).andExpect(view().name("student/resources"));
            mockMvc.perform(get("/student/notifications").with(user("student").roles("STUDENT")))
                .andExpect(status().isOk()).andExpect(view().name("student/notifications"));
            mockMvc.perform(get("/admin/users").with(user("admin").roles("SUPER_ADMIN")))
                .andExpect(status().isOk()).andExpect(view().name("admin/users"));
            mockMvc.perform(get("/admin/users").with(user("student").roles("STUDENT")))
                .andExpect(status().isForbidden());
        }

        @Test void focusAndSeatWorkspacesRespectPortalRoles() throws Exception {
            mockMvc.perform(get("/student/planner").with(user("student").roles("STUDENT")))
                .andExpect(status().isOk()).andExpect(view().name("student/planner"));
            mockMvc.perform(get("/student/seats").with(user("student").roles("STUDENT")))
                .andExpect(status().isOk()).andExpect(view().name("student/seats"));
            mockMvc.perform(get("/admin/study-plans").with(user("admin").roles("SUPER_ADMIN")))
                .andExpect(status().isOk()).andExpect(view().name("admin/study-plans"));
            mockMvc.perform(get("/admin/seat-bookings").with(user("admin").roles("SUPER_ADMIN")))
                .andExpect(status().isOk()).andExpect(view().name("admin/seat-bookings"));
            mockMvc.perform(get("/student/planner").with(user("admin").roles("SUPER_ADMIN")))
                .andExpect(status().isForbidden());
        }
}
