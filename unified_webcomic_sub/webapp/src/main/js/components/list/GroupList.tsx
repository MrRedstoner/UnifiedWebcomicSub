'use strict';

import React, { useState, useEffect } from 'react';
import { Group, UserPermissionClosure } from '../../api/entities'
import { GROUP_SERVICE_READ } from '../../api/apiEndpoints'
import { asyncFetchGet } from '../../api/apiCall'
import InputBox from '../InputBox'
import { useNavigate } from 'react-router-dom';

const pageSize = 5;

type RowProps = {
	source: Group,
	onRow: (id: number) => void
}
const SourceListRow: React.FC<RowProps> = ({ source, onRow }) => {
	return (
		<tr onClick={() => { onRow(source.id) }}>
			<td>{source.id}</td>
			<td>{source.name}</td>
			<td>{source.description}</td>
		</tr>
	);
}

const renderRows = (error: string, isLoaded: boolean, sourceList: Group[], onRow: (id: number) => void) => {
	if (error) {
		return <><tr><td colSpan={3} className="allignCenter">Error: {error}</td></tr></>;
	} else if (!isLoaded) {
		return <><tr><td colSpan={3} className="allignCenter">Loading...</td></tr></>;
	} else {
		return sourceList.map((s) => <SourceListRow source={s} onRow={onRow} key={s.id} />);
	}
}

type SearchArgs = {
	id?: string;
	name?: string;
	description?: string;
};

type PageArgs = {
	number: number;
}

const firstPage: PageArgs = { number: 0 };

type SourceList = {
	total: number;
	items: Group[];
}

type Props = {
	user: UserPermissionClosure;
}

const GroupList: React.FC<Props> = ({ user }) => {
	const navigate = useNavigate();
	const [error, setError] = useState<string>(null);
	const [isLoaded, setIsLoaded] = useState(false);
	const [sourceList, setSourceList] = useState<SourceList>({ total: 0, items: [] });
	const [search, setSearch] = useState<SearchArgs>({})
	const [page, setPage] = useState<PageArgs>(firstPage);
	const onRow=(source:number)=>{
		navigate("/groups/show/" + source);
	}
	const onNew = () => {
		navigate("/groups/new");
	}

	const newBtn = <button disabled={!user.createSource} onClick={onNew}>New</button>

	const loadData = async () => {
		setIsLoaded(false);
		setError(null);
		const data: any = Object.assign({}, search);//TODO nicely
		data.limit = pageSize;
		data.offset = page.number * pageSize;
		await asyncFetchGet(GROUP_SERVICE_READ, data, setSourceList, setError, setIsLoaded);
	};

	useEffect(() => {
		loadData().catch(console.error);
	}, []);

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

	const onDescription = (description: string) => {
		const newSearch = Object.assign({}, search);
		if (description === "") {
			delete newSearch.description;
		} else {
			newSearch.description = description;
		}
		setSearch(newSearch);
	};

	const prevPage = () => {
		const newPage = { number: page.number - 1 };
		setPage(newPage);
	}
	const nextPage = () => {
		const newPage = { number: page.number + 1 };
		setPage(newPage);
	}
	const lastPage = () => {
		const newPage = { number: Math.ceil(sourceList.total / pageSize) - 1 };
		setPage(newPage);
	}
	//TODO set first page and reload on enter in filter, reload on page buttons
	const rows = renderRows(error, isLoaded, sourceList.items, onRow);
	const first = <button disabled={page.number === 0} onClick={() => setPage(firstPage)}>First</button>
	const prev = <button disabled={page.number === 0} onClick={prevPage}>Previous</button>;
	const next = <button disabled={((page.number + 1) * pageSize) >= sourceList.total} onClick={nextPage}>Next</button>;
	const last = <button disabled={((page.number + 1) * pageSize) >= sourceList.total} onClick={lastPage}>Last</button>;

	return (<>
		{newBtn}
		<div>
			<p>Showing page {page.number + 1} of {sourceList.total} items</p>
			{first}
			{prev}
			{next}
			{last}
			<button onClick={loadData}>Reload</button>
			<table>
				<thead>
					<tr>
						<th>
							<p>ID</p>
							<InputBox initialValue="" setValue={onId} className="idcolumn" />
						</th>
						<th>
							<p>Name</p>
							<InputBox initialValue="" setValue={onName} />
						</th>
						<th>
							<p>Description</p>
							<InputBox initialValue="" setValue={onDescription} />
						</th>
					</tr>
				</thead>
				<tbody>
					{rows}
				</tbody>
			</table>
		</div>
		</>);
};

export default GroupList;