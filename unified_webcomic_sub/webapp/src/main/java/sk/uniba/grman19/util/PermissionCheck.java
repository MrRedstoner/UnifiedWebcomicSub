package sk.uniba.grman19.util;

import sk.uniba.grman19.models.entity.UWSUser;

public class PermissionCheck {
	public static final boolean isOwner(UWSUser user) {
		return user.getOwner();
	}

	public static final boolean isAdmin(UWSUser user) {
		return isOwner(user) || user.getAdmin();
	}

	public static final boolean canCreateSource(UWSUser user) {
		return isAdmin(user) || user.getCreateSource();
	}

	public static final boolean canEditSource(UWSUser user) {
		return canCreateSource(user) || user.getEditSource();
	}

	public static final boolean canEditGroup(UWSUser user) {
		return isAdmin(user) || user.getEditGroup();
	}

	public static final boolean canCreatePost(UWSUser user) {
		return isAdmin(user) || user.getCreatePost();
	}
}
