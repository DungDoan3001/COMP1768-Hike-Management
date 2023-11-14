using Hikemaui.Models;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace Hikemaui.Views
{
	[XamlCompilation(XamlCompilationOptions.Compile)]
	public partial class ObservationDetails : ContentPage
	{
		public ObservationDetails(ObservationModel observationModel)
		{
			InitializeComponent();
			BindingContext = observationModel;
		}
	}
}