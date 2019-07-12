package io.choerodon.hap.iam.app.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.choerodon.hap.iam.api.query.RoleQuery;
import io.choerodon.hap.iam.api.validator.RoleValidator;
import io.choerodon.hap.iam.app.service.ChoerodonRoleService;
import io.choerodon.hap.iam.app.service.RolePermissionService;
import io.choerodon.hap.iam.exception.ChoerodonRoleException;
import io.choerodon.hap.iam.infra.asserts.asserts.RoleAssertHelper;
import io.choerodon.hap.iam.infra.dto.PermissionDTO;
import io.choerodon.hap.iam.infra.dto.RoleDTO;
import io.choerodon.hap.iam.infra.dto.RolePermissionDTO;
import io.choerodon.hap.iam.infra.mapper.ChoerodonRoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static io.choerodon.hap.iam.exception.ChoerodonRoleException.ERROR_ROLE_NOT_EXISTED;
import static io.choerodon.hap.iam.exception.ChoerodonRoleException.ERROR_ROLE_UPDATE;

/**
 * @author qiang.zeng
 * @since 2019/5/21
 */
@Service
public class ChoerodonRoleServiceImpl implements ChoerodonRoleService {


    private ChoerodonRoleMapper roleMapper;

    private RolePermissionService rolePermissionService;

    private RoleAssertHelper roleAssertHelper;

    public ChoerodonRoleServiceImpl(ChoerodonRoleMapper roleMapper,
                                    RolePermissionService rolePermissionService, RoleAssertHelper roleAssertHelper) {

        this.roleMapper = roleMapper;
        this.rolePermissionService = rolePermissionService;
        this.roleAssertHelper = roleAssertHelper;
    }

    @Override
    public List<Long> selectEnableRoleIdsByMemberIdAndMemberType(Long memberId, String memberType) {
        return roleMapper.selectEnableRoleIdsByMemberIdAndMemberType(memberId, memberType);
    }

    @Override
    public List<RoleDTO> selectEnableRolesInfoByMemberIdAndMemberType(Long memberId, String memberType) {
        return roleMapper.selectEnableRolesInfoByMemberIdAndMemberType(memberId, memberType);
    }

    @Override
    public List<RoleDTO> selectRolesAssignToUserByUserId(Long userId) {
        return roleMapper.selectRolesAssignToUserByUserId(userId);
    }

    @Override
    public List<RoleDTO> selectRolesNotAssignToUser(RoleDTO role) {
        return roleMapper.selectRolesNotAssignToUser(role);
    }

    @Override
    public PageInfo<RoleDTO> pagingSearch(int page, int size, RoleQuery roleQuery) {
        return PageHelper.startPage(page, size).doSelectPageInfo(() -> roleMapper.fulltextSearch(roleQuery));
    }

    @Override
    public RoleDTO create(RoleDTO role) throws ChoerodonRoleException {
        RoleValidator.insertOrUpdateCheck(role);
        role.setBuiltIn(false);
        role.setEnabled(true);
        role.setEnableForbidden(true);
        role.setModified(true);
        roleAssertHelper.codeExisted(role.getCode());
        roleMapper.insertSelective(role);
        role.setPermissions(role.getPermissions());
        //维护role_permission关系
        insertRolePermission(role);
        return role;

    }

    @Transactional(rollbackFor = ChoerodonRoleException.class)
    @Override
    public RoleDTO update(RoleDTO roleDTO) throws ChoerodonRoleException {
        RoleValidator.insertOrUpdateCheck(roleDTO);
        RoleDTO role = roleMapper.selectByPrimaryKey(roleDTO);
        if (role == null) {
            throw new ChoerodonRoleException(ERROR_ROLE_NOT_EXISTED, roleDTO.getName());
        }
        //内置的角色不允许更新字段，只能更新label
        if (role.getBuiltIn()) {
            return roleDTO;
        } else {
            if (roleMapper.updateByPrimaryKeySelective(role) != 1) {
                throw new ChoerodonRoleException(ERROR_ROLE_UPDATE);
            }
            role.setPermissions(roleDTO.getPermissions());
            //维护role_permission关系
            updateRolePermission(role);
            return role;
        }
    }

    @Override
    public void delete(Long id) throws ChoerodonRoleException {
        RoleDTO roleDTO = roleAssertHelper.roleNotExisted(id);
        roleAssertHelper.roleIsBuiltIn(roleDTO.getBuiltIn());
        roleMapper.deleteByPrimaryKey(id);
        rolePermissionService.deleteByRoleId(id);
    }


    @Override
    public RoleDTO enableRole(Long id) throws ChoerodonRoleException {
        return disableOrEnableRole(id, true);
    }

    @Override
    public RoleDTO disableRole(Long id) throws ChoerodonRoleException {
        return disableOrEnableRole(id, false);
    }

    @Override
    public void check(RoleDTO role) throws ChoerodonRoleException {
        Boolean checkCode = !StringUtils.isEmpty(role.getCode());
        if (!checkCode) {
            throw new ChoerodonRoleException("error.role.code.empty");
        }
        Boolean createCheck = StringUtils.isEmpty(role.getId());
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setCode(role.getCode());
        if (createCheck) {
            Boolean existed = roleMapper.selectOne(roleDTO) != null;
            if (existed) {
                throw new ChoerodonRoleException("error.role.code.exist");
            }
        } else {
            Long id = role.getId();
            RoleDTO roleDTO1 = roleMapper.selectOne(roleDTO);
            Boolean existed = roleDTO1 != null && !id.equals(roleDTO1.getId());
            if (existed) {
                throw new ChoerodonRoleException("error.role.code.exist");
            }
        }
    }

    @Override
    public RoleDTO queryWithPermissions(Long id) {
        return roleMapper.roleWithPermissions(id);
    }

    @Override
    public RoleDTO selectByCode(String code) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setCode(code);
        return roleMapper.selectOne(roleDTO);
    }

    @Override
    public List<RoleDTO> fuzzyQueryRolesByRoleName(String roleName) {
        RoleQuery roleQuery = new RoleQuery();
        roleQuery.setName(roleName);
        return roleMapper.fulltextSearch(roleQuery);
    }

    /**
     * 插入角色权限.
     *
     * @param role 角色DTO(包含权限)
     */
    private void insertRolePermission(RoleDTO role) {
        for (PermissionDTO permission : role.getPermissions()) {
            RolePermissionDTO rolePermissionDTO = new RolePermissionDTO();
            rolePermissionDTO.setPermissionId(permission.getId());
            rolePermissionDTO.setRoleId(role.getId());
            rolePermissionService.insertSelective(rolePermissionDTO);
        }
    }

    /**
     * 更新角色权限.
     *
     * @param role 角色DTO
     */
    private void updateRolePermission(RoleDTO role) {
        Long roleId = role.getId();
        List<PermissionDTO> permissions = role.getPermissions();
        RolePermissionDTO rolePermissionDTO = new RolePermissionDTO();
        rolePermissionDTO.setRoleId(roleId);
        List<RolePermissionDTO> existingRolePermissions = rolePermissionService.select(rolePermissionDTO);
        List<Long> existingPermissionId =
                existingRolePermissions.stream().map(RolePermissionDTO::getPermissionId).collect(Collectors.toList());
        List<Long> newPermissionId =
                permissions.stream().map(PermissionDTO::getId).collect(Collectors.toList());
        //permissionId交集
        List<Long> intersection = existingPermissionId.stream().filter(newPermissionId::contains).collect(Collectors.toList());
        //删除的permissionId集合
        List<Long> deleteList = existingPermissionId.stream().filter(item ->
                !intersection.contains(item)).collect(Collectors.toList());
        //新增的permissionId集合
        List<Long> insertList = newPermissionId.stream().filter(item ->
                !intersection.contains(item)).collect(Collectors.toList());
        insertList.forEach(permissionId -> {
            RolePermissionDTO rp = new RolePermissionDTO();
            rp.setRoleId(roleId);
            rp.setPermissionId(permissionId);
            rolePermissionService.insertSelective(rp);
        });
        deleteList.forEach(permissionId -> {
            RolePermissionDTO rp = new RolePermissionDTO();
            rp.setRoleId(roleId);
            rp.setPermissionId(permissionId);
            rolePermissionService.delete(rp);
        });
    }


    /**
     * 启用或禁用角色.
     *
     * @param id      角色Id
     * @param enabled 是否启用
     * @return 角色DTO
     * @throws ChoerodonRoleException 角色不存在异常或角色更新异常
     */
    private RoleDTO disableOrEnableRole(Long id, boolean enabled) throws ChoerodonRoleException {
        RoleDTO roleDTO = roleMapper.selectByPrimaryKey(id);
        if (roleDTO == null) {
            throw new ChoerodonRoleException(ERROR_ROLE_NOT_EXISTED);
        }
        roleDTO.setEnabled(enabled);
        if (roleMapper.updateByPrimaryKeySelective(roleDTO) != 1) {
            throw new ChoerodonRoleException(ERROR_ROLE_UPDATE);
        }
        return roleDTO;
    }
}
