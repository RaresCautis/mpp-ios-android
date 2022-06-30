import UIKit
import SharedCode

class TableViewCell: UITableViewCell {
    @IBOutlet weak var departureTime: UILabel!
    @IBOutlet weak var departureDate: UILabel!
    @IBOutlet weak var arrivalTime: UILabel!
    @IBOutlet weak var arrivalDate: UILabel!
}

class ViewController: UIViewController, ApplicationContractView {

    @IBOutlet weak var departurePicker: UIPickerView!
    @IBOutlet weak var arrivalPicker: UIPickerView!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var datePicker: UIDatePicker!
    
    private let tableViewCell: String = "TableViewCell"
    
    private var tableData: [DepartureInformation] = [DepartureInformation]()
    
    private var stations: [String] = [String]()
    

    private let presenter: ApplicationContractPresenter = ApplicationPresenter()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.onViewTaken(view: self)
        
        setUpPickers()
        setUpTable()
    }
    
    
    @IBAction func submitButtonTapped() {
        let originStation = stations[departurePicker.selectedRow(inComponent:0)]
        let finalStation = stations[arrivalPicker.selectedRow(inComponent:0)]
        let dateTime = convertDateToCorrectFormat(date: datePicker.date)
        print(datePicker.date.description)
        presenter.makeTrainSearch(originCrs: originStation, destinationCrs: finalStation, dateTime: dateTime)
    }
}

extension ViewController {
    func convertDateToCorrectFormat(date: Date) -> String {
        let dateString = date.description
        let date = dateString.prefix(10)
        
        let start = dateString.index(dateString.startIndex, offsetBy: 11)
        let end = dateString.index(dateString.startIndex, offsetBy: 18)
        let time = String(dateString[start...end])
        
        return date + "T" + time + ".000+00:00"
    }
}

extension ViewController {
    func setStationNames(stationNames: Array<String>) {
        stations = stationNames
    }
}

extension ViewController: UIPickerViewDelegate, UIPickerViewDataSource {
    
    func setUpPickers() {
        self.departurePicker.delegate = self
        self.departurePicker.dataSource = self
        
        self.arrivalPicker.delegate = self
        self.arrivalPicker.dataSource = self
    }
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView:UIPickerView, numberOfRowsInComponent component:Int) -> Int {
        return stations.count
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return stations[row]
    }
}


extension ViewController: UITableViewDataSource, UITableViewDelegate {
    
    func setUpTable() {
        self.tableView.delegate = self
        self.tableView.dataSource = self
        
        registerTableViewCells()
    }
    
    func registerTableViewCells() {
        let trainCell = UINib(nibName: tableViewCell, bundle: nil)
        self.tableView.register(trainCell, forCellReuseIdentifier: tableViewCell)
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) ->
        UITableViewCell {
        if let cell = tableView.dequeueReusableCell(withIdentifier: tableViewCell, for: indexPath) as? TableViewCell{
            cell.departureTime.text = tableData[indexPath.row].departureDateTime.time
            cell.departureDate.text = tableData[indexPath.row].departureDateTime.date
            cell.arrivalTime.text = tableData[indexPath.row].arrivalDateTime.time
            cell.arrivalDate.text = tableData[indexPath.row].arrivalDateTime.date
            return cell
        }
        
        return UITableViewCell()
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tableData.count
    }
    
    func setTableData(data: [DepartureInformation]) {
        tableData = data
        self.tableView.reloadData()
    }
}

extension ViewController{
    
    func createAlert(alertMessage: String, alertTitle: String){
        let alert = UIAlertController(title: alertTitle, message: alertMessage, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "Okay", style: .default, handler: nil))
        self.present(alert, animated: true, completion: nil)
    }
}
