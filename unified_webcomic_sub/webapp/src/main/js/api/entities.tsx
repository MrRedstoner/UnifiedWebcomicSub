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

export { UWSUser, MailSettings, MailSettingsChange }