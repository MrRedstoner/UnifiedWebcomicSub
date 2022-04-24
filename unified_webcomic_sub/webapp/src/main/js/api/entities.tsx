'use strict';

type UWSUser = {
	id: number;
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
	subscribed?: boolean;
	ignored?: boolean;
}

type Group = {
	id: number;
	name: string;
	description: string;
	subscribed?: boolean;
	children?: GroupChild[];
	sources?: SourceSubscription[];
	posters?: PostSubscription[];
}

type GroupChild = {
	child: Group;
}

type SourceSubscription = {
	group: Group;
	source: Source;
}

type PostSubscription = {
	group: Group;
	user: UWSUser;
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

type PostOption = {
	id: number;
	content: string;
}

type Post = {
	id?: number;
	title: string;
	content: string;
	options?: PostOption[]
}

export {
	UWSUser,
	MailSettings,
	MailSettingsChange,
	Source,
	Group,
	GroupChild,
	SourceSubscription,
	PostSubscription,
	UserPermissionClosure,
	PostOption,
	Post,
}