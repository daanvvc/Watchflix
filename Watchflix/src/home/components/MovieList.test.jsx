import { expect, describe, vi } from 'vitest'
import { render, screen, waitFor } from '@testing-library/react'
import MovieList from "./MovieList.jsx"
import MovieApi from '../../api/MovieApi.jsx'
import { BrowserRouter } from 'react-router-dom'
import '@testing-library/jest-dom'

vi.mock('./MovieApi', () => ({
    default: {
      getMovies: vi.fn(),
    },
}))

vi.mock('../../api/TokenManager', () => ({
  default: {
    getAccessToken: () => 'fake-token',
  },
}))

describe('movie_list_test', () => {

    afterEach(() => {
        vi.clearAllMocks()
    })

    it('testing tests', async () => {
        expect(true).toBe(true)
    })    

    it('tests movielist', async () => {
        const titleOne = "The Brutalist"
        const mockData = { data: [{ name: titleOne }, { name: "Memoir of a snail" }]}
          
        vi.spyOn(MovieApi, 'getMovies').mockResolvedValue(mockData);

        render(
            <BrowserRouter>
              <MovieList />
            </BrowserRouter>
          )
      
        await waitFor(() => {
            expect(screen.getByText(titleOne)).toBeInTheDocument()
          })
    })
})