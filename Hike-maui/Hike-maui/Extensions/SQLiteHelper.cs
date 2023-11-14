using System.Collections.Generic;
using System.Threading.Tasks;
using Hikemaui.Models;
using SQLite;

namespace Hikemaui.Extensions
{
	public class SQLiteHelper
	{
        private readonly SQLiteAsyncConnection _database;
        public SQLiteHelper(string databasePath)
        {
            _database = new SQLiteAsyncConnection(databasePath);
            _database.CreateTableAsync<HikeModel>();
            _database.CreateTableAsync<ObservationModel>();
        }

        // HIKE
        public async Task<int> CreateHikeAsync(HikeModel hike)
        {
            return await _database.InsertAsync(hike);
        }

        public async Task<List<HikeModel>> GetAllHikeAsync()
        {
            return await _database.Table<HikeModel>().ToListAsync();
        }

        public async Task<int> UpdateHikeAsync(HikeModel hike)
        {
            return await _database.UpdateAsync(hike);
        }
		public async Task<int> DeleteHikeAsync(HikeModel hike)
		{
			return await _database.DeleteAsync(hike);
		}

		public async Task<List<HikeModel>> SearchAsync(string query)
        {
            var result = await _database.Table<HikeModel>()
                .Where(hike => hike.Name.Contains(query) ||
							   hike.Level.Contains(query) ||
							   hike.Location.Contains(query) ||
							   hike.Date.Contains(query))
                .ToListAsync();

            return result;
        }


        // Observation
        public async Task<List<ObservationModel>> GetObservationsByHikeIdAsync(int hikeID)
        {
            return await _database.Table<ObservationModel>()
                .Where(observation => observation.HikeId.Equals(hikeID))
                .ToListAsync();
        }

		public async Task<int> CreateObservationAync(ObservationModel observation)
		{
			return await _database.InsertAsync(observation);
		}

		public async Task<int> UpdateObservationAsync(ObservationModel observation)
		{
			return await _database.UpdateAsync(observation);
		}

		public async Task<int> DeleteObservationAsync(ObservationModel observation)
		{
			return await _database.DeleteAsync(observation);
		}

	}
}
