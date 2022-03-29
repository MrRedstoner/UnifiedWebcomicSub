'use strict';

type UWSUser = {
	name: string;
	mailSettings: MailSettings;
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
}

export { UWSUser, MailSettings, MailSettingsChange, Source, Group }