'use strict';

import { UWSUser, UserPermissionClosure } from "./entities";

const ownerClosure: UserPermissionClosure = {
	registered: true, owner: true, admin: true, createPost: true, createSource: true, editSource: true, editGroup: true
}
const adminClosure: UserPermissionClosure = {
	registered: true, owner: false, admin: true, createPost: true, createSource: true, editSource: true, editGroup: true
}
const emptyClosure: UserPermissionClosure = {
	registered: false, owner: false, admin: false, createPost: false, createSource: false, editSource: false, editGroup: false
}

const getPermissionClosure: (user: UWSUser) => UserPermissionClosure = (user) => {
	if (user == null) {
		return emptyClosure;
	} else if (user.owner) {
		return ownerClosure;
	} else if (user.admin) {
		return adminClosure;
	} else {
		return ({
			registered: true,
			owner: false,
			admin: false,
			createPost: user.createPost,
			createSource: user.createSource,
			editSource: user.editSource || user.createSource,
			editGroup: user.editGroup
		});
	}
}

export { getPermissionClosure };