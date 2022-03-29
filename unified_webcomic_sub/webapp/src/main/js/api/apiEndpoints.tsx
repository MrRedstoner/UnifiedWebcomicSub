'use strict';

const USER_SERVICE = "/rest/user";
const GET_LOGGED_IN_USER = USER_SERVICE + "/getlogged";

const MAIL_SERVICE = "/rest/mail";
const GET_MAIL_SETTINGS = MAIL_SERVICE + "/getmailset";
const SET_MAIL_SETTINGS = MAIL_SERVICE + "/setmailset";

const SOURCE_SERVICE = "/rest/source";
const SOURCE_SERVICE_READ = SOURCE_SERVICE + "/read"

export { GET_LOGGED_IN_USER, GET_MAIL_SETTINGS, SET_MAIL_SETTINGS, SOURCE_SERVICE_READ };