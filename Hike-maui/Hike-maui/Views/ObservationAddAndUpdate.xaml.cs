using System;
using Hike_maui;
using Hikemaui.Extensions;
using Hikemaui.Models;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace Hikemaui.Views
{
	[XamlCompilation(XamlCompilationOptions.Compile)]
	public partial class ObservationAddAndUpdate : ContentPage
	{
		private readonly HikeModel _hikeModel;
		private readonly ObservationModel _observationModel;

		private readonly SQLiteHelper _dbHelper;


		public ObservationAddAndUpdate(HikeModel hikeModel)
		{
			InitializeComponent();
			_hikeModel = hikeModel;
			_dbHelper = App.DatabaseHelper;
		}

		public ObservationAddAndUpdate(ObservationModel observationModel)
		{
			InitializeComponent();
			string[] dateTime = observationModel.Time.Split(',');
			Title = "Edit Observation";

			_dbHelper = App.DatabaseHelper;

			_observationModel = observationModel;
			nameEntry.Text = _observationModel.Name;
			datePicker.Date = DateTime.Parse(dateTime[1]);
			timePicker.Time = TimeSpan.Parse(dateTime[0]);
			commentEntry.Text = _observationModel.Comment;

			nameEntry.Focus();
		}

		private async void Button_Clicked(object sender, EventArgs e)
		{
            if (string.IsNullOrEmpty(nameEntry.Text))
            {
				await DisplayAlert("Invalid", "Blank or Whitespace value is Invalid", "OK");
            }
			else if (_observationModel != null) {
				EditObservation();
			} 
			else
			{
				AddNewObservation();
			}
        }

		private async void EditObservation()
		{
			string dateTime = $"{timePicker.Time}, {datePicker.Date.ToShortDateString()}";

			_observationModel.Name = nameEntry.Text;
			_observationModel.Time = dateTime;
			_observationModel.Comment = commentEntry.Text;

			await _dbHelper.UpdateObservationAsync(_observationModel);
			await Navigation.PopAsync();
		}

		private async void AddNewObservation()
		{
			DateTime date = datePicker.Date;
			var time = timePicker.Time;

			await _dbHelper.CreateObservationAync(new ObservationModel
			{
				HikeId = _hikeModel.Id,
				Name = nameEntry.Text,
				Time = $"{time}, {date.ToShortDateString()}",
				Comment = commentEntry.Text,
				CreatedAt = DateTime.Now.ToString("hh:mm"),
				UpdatedAt = DateTime.Now.ToString("hh:mm"),
			});

			await Navigation.PopAsync();
		}
	}
}