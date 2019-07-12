package io.choerodon.hap.iam.infra.asserts.asserts;

import io.choerodon.hap.iam.infra.dto.MenuDTO;
import io.choerodon.hap.iam.infra.mapper.MenuMapper;
import io.choerodon.base.exception.BaseRuntimeException;
import org.springframework.stereotype.Component;

/**
 * 菜单断言帮助类.
 *
 * @author superlee
 */
@Component
public class MenuAssertHelper {
    private MenuMapper menuMapper;

    public MenuAssertHelper(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }

    public void codeExisted(String code) {
        MenuDTO dto = new MenuDTO();
        dto.setCode(code);
        if (!menuMapper.select(dto).isEmpty()) {
            throw new BaseRuntimeException("error.menu.code.existed");
        }
    }

    public MenuDTO menuNotExisted(Long id) {
        MenuDTO dto = menuMapper.selectByPrimaryKey(id);
        if (dto == null) {
            throw new BaseRuntimeException("error.menu.not.exist", id);
        }
        return dto;
    }
}
