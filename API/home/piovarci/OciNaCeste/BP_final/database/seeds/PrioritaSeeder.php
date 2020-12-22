<?php

use App\Models\Priorita;
use Illuminate\Database\Seeder;

class PrioritaSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $priority = [
            [
                'priorita' => 'Nepiradena',
            ],
            [
                'priorita' => 'Vysoka',
            ],
            [
                'priorita' => 'Stredna',
            ],
            [
                'priorita' => 'Nizka',
            ]
        ];

        foreach ($priority as $priorita) {
            Priorita::create(array(
                'priorita' => $priorita['priorita']
            ));
        }
    }
}
