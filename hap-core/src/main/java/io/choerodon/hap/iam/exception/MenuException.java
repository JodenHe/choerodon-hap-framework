package io.choerodon.hap.iam.exception;

import io.choerodon.base.exception.BaseException;

/**
 * 菜单异常.
 *
 * @author qiang.zeng
 * @since 2019/5/20
 */
public class MenuException extends BaseException {

    private static final long serialVersionUID = 1L;


    /**
     * 目录编码为空.
     */
    public static final String ERROR_MENU_CODE_EMPTY = "error.menu.code.empty";

    /**
     * 目录名称为空.
     */
    public static final String ERROR_MENU_NAME_EMPTY = "error.menu.name.empty";

    /**
     * 图标为空.
     */
    public static final String ERROR_MENU_ICON_EMPTY = "error.menu.icon.empty";
    /**
     * 父级编码为空.
     */
    public static final String ERROR_MENU_PARENT_CODE_EMPTY = "error.menu.parent.code.empty";
    /**
     * 目录编码已存在.
     */
    public static final String ERROR_MENU_CODE_EXISTED = "error.menu.code.existed";
    /**
     * 默认菜单，不能删除.
     */
    public static final String ERROR_MENU_DEFAULT = "error.menu.default";
    /**
     * 顶级菜单不存在.
     */
    public static final String ERROR_MENU_TOP_NOT_EXISTED = "error.menu.top.not.existed";


    public MenuException(String code, Object... parameters) {
        super(code, code, parameters);
    }

    public MenuException(String code, String message, Object... parameters) {
        super(code, message, parameters);
    }

}
