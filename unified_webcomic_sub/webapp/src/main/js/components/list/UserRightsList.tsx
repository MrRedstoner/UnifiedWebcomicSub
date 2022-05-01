'use strict';

import React, { useState } from 'react';
import InputBox from '../InputBox';
import PaginatedTable, { Itemlist, PageArgs } from '../table/PaginatedTable';
import { asyncFetchGetState } from '../../api/apiCall';
import { USER_SERVICE_USERS_READ } from '../../api/apiEndpoints';
import { useNavigate } from 'react-router-dom';
import { UWSUser } from '../../api/entities';

const displayBool = (value:boolean) => {
	if(value){
		return "Y";
	} else {
		return "N";
	}
}

type SearchArgs = {
	id?: string;
	name?: string;
};

const UserRightsList: React.FC = () => {
	const navigate = useNavigate();
	const [search, setSearch] = useState<SearchArgs>({});

	const onId = (id: string) => {
		const newSearch = Object.assign({}, search);
		if (id === "") {
			delete newSearch.id;
		} else {
			newSearch.id = id;
		}
		setSearch(newSearch);
	};

	const onName = (name: string) => {
		const newSearch = Object.assign({}, search);
		if (name === "") {
			delete newSearch.name;
		} else {
			newSearch.name = name;
		}
		setSearch(newSearch);
	};

	const cols = [
		{
			key: "id",
			field: (item:UWSUser)=>item.id,
			content: <div><p>ID</p><InputBox initialValue="" setValue={onId} /></div>
		},
		{
			key: "name",
			field: (item:UWSUser)=>item.name,
			content: <div><p>Name</p><InputBox initialValue="" setValue={onName} /></div>
		},
		{
			key: "owner",
			field: (item:UWSUser)=>displayBool(item.owner),
			content: <div><p>Owner</p></div>
		},
		{
			key: "admin",
			field: (item:UWSUser)=>displayBool(item.admin),
			content: <div><p>Admin</p></div>
		},
		{
			key: "createPost",
			field: (item:UWSUser)=>displayBool(item.createPost),
			content: <div><p>Post</p></div>
		},
		{
			key: "createSource",
			field: (item:UWSUser)=>displayBool(item.createSource),
			content: <div><p>Source</p></div>
		},
		{
			key: "editSource",
			field: (item:UWSUser)=>displayBool(item.editSource),
			content: <div><p>Source edit</p></div>
		},
		{
			key: "editGroup",
			field: (item:UWSUser)=>displayBool(item.editGroup),
			content: <div><p>Group edit</p></div>
		}
	]

	const loadData = (page: PageArgs, setUsers: (itemList:Itemlist)=>void, setError:(error:string)=>void, )=>{
		const data:any = Object.assign({
			limit:page.pageSize,
			offset: page.number * page.pageSize,
		}, search);
		asyncFetchGetState(USER_SERVICE_USERS_READ, data, setUsers, setError)
	}

	const onRow=(user:UWSUser)=>{
		navigate("/users/show/" + user.id);
	}

	return (<PaginatedTable columns={cols} load={loadData} onRow={onRow}/>);
}

export default UserRightsList;