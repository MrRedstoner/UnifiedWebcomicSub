'use strict';

type UWSUser = {
	name: string;
	mailSettings: MailSettings;
	owner: boolean;
	admin: boolean;
	createPost: boolean;
	createSource: boolean;
	editSource: boolean;
	editGroup: boolean;
};

type MailSettings = {
	mailAddress: string;
	daily: boolean;
	weekly: boolean;
	dayOfWeek: number;
}

type MailSettingsChange = {
	mailAddress?: string;
	daily?: boolean;
	weekly?: boolean;
	dayOfWeek?: number;
}

type Source = {
	id: number;
	name: string;
	description: string;
}

type Group = {
	id: number;
	name: string;
	description: string;
	parents: GroupChild[];
	children: GroupChild[];
}

type GroupChild = {
	child: Group;
}

type UserPermissionClosure = {
	registered: boolean;
	owner: boolean;
	admin: boolean;
	createPost: boolean;
	createSource: boolean;
	editSource: boolean;
	editGroup: boolean;
};

export {
	UWSUser,
	MailSettings,
	MailSettingsChange,
	Source,
	Group,
	GroupChild,
	UserPermissionClosure
}