package sdg.blog.protectids.service;

import org.springframework.stereotype.Service;

/**
 * For demonstration purposes only (used by UserServiceTest)
 */
@Service
public class AuditService {

    public void auditUpdate(Class updatedType, Long id) {
        // Log the update to the class in the audit log
    }
}
