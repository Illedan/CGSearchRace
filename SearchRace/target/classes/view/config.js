import { GraphicEntityModule } from './entity-module/GraphicEntityModule.js';
import { EndScreenModule } from './endscreen-module/EndScreenModule.js';
import { TooltipModule } from './tooltip-module/TooltipModule.js';
import { DebugModule, api2 } from './debug/DebugModule.js';

export const modules = [
	GraphicEntityModule,
	TooltipModule,
    EndScreenModule,
    DebugModule
];

export const gameName = 'Search Race';

export const options = [
{
  title: 'DEBUG',
  get: function () {
    return api2.showDebug
  },
  set: function (value) {
    api2.showDebug = value
  },
  values: {
    'ON': true,
    'OFF': false
  }
}
]